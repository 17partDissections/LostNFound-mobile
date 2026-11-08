package com.q17pd.lostfound;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.q17pd.lostfound.api.ApiClient;
import com.q17pd.lostfound.api.Requests;
import com.q17pd.lostfound.api.StatusResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private static final int MAX_AVATAR_BYTES = 5 * 1024 * 1024;
    private TextView tvName, tvPhone, tvContact;
    private ShapeableImageView ivAvatar;
    private User user;
    private long avatarVersion = 0;
    private final ActivityResultLauncher<String> pickAvatar = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) uploadAvatar(uri);
            });
    public UserFragment() {
        super(R.layout.fragment_user);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvUserName);
        tvPhone = view.findViewById(R.id.tvUserPhone);
        tvContact = view.findViewById(R.id.tvUserContact);
        ivAvatar = view.findViewById(R.id.ivUserAvatar);

        ivAvatar.setOnClickListener(v -> {
            if (ensureLoaded()) pickAvatar.launch("image/*");
        });
        view.findViewById(R.id.btnEditName).setOnClickListener(v -> {
            if (ensureLoaded()) showEditNameDialog();
        });
        view.findViewById(R.id.btnEditContact).setOnClickListener(v -> {
            if (ensureLoaded()) showEditContactDialog();
        });
        view.findViewById(R.id.btnChangePassword).setOnClickListener(v -> {
            if (ensureLoaded()) showChangePasswordDialog();
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (Session.isLoggedIn(requireContext())) {
            loadProfile();
        } else {
            user = null;
            tvName.setText("");
            tvPhone.setText("");
            tvContact.setText("");
            ivAvatar.setImageResource(R.drawable.ic_person_outline);
        }
    }
    // ---------- Загрузка профиля ----------
    private void loadProfile() {
        int myId = Session.getUserId(requireContext());
        // TODO: заменить на GET users/{id}, когда он появится на сервере
        ApiClient.get().getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (!isAdded()) return;

                List<User> users = response.body();
                if (!response.isSuccessful() || users == null) {
                    toast("Не удалось загрузить профиль");
                    return;
                }
                for (User u : users) {
                    if (u.getId() == myId) {
                        user = u;
                        bind();
                        return;
                    }
                }
                toast("Профиль не найден на сервере");
            }
            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                if (isAdded()) toast("Нет соединения с сервером");
            }
        });
    }
    private void bind() {
        tvName.setText(user.getFullName());
        tvPhone.setText("+" + user.getPhoneNum());
        tvContact.setText(user.getAdditionalContact().isEmpty() ? "не указан" : user.getAdditionalContact());

        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl != null) {
            Glide.with(this)
                    .load(avatarUrl)
                    .signature(new ObjectKey(avatarVersion))
                    .placeholder(R.drawable.ic_person_outline)
                    .error(R.drawable.ic_person_error)
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.drawable.ic_person_outline);
        }
    }
    private boolean ensureLoaded() {
        if (user == null) {
            toast("Профиль еще не загружен");
            return false;
        }
        return true;
    }
    // ---------- Изменение данных ----------
    private void showEditNameDialog() {
        LinearLayout box = dialogBox();
        EditText etName = addField(box, "Имя", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS, user.getName());
        EditText etSurname = addField(box, "Фамилия", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS, user.getSurname());
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Изменить имя")
                .setView(box)
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Сохранить", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String surname = etSurname.getText().toString().trim();
                    if (name.isEmpty() || surname.isEmpty()) {
                        toast("Имя и фамилия не должны быть пустыми");
                        return;
                    }
                    send(ApiClient.get().editName(user.getId(), new Requests.EditName(name, surname)),
                            "Имя сохранено", "Не удалось сохранить имя",
                            () -> {
                                user.setFullName(name, surname);
                                bind();
                            });
                })
                .show();
    }
    private void showEditContactDialog() {
        LinearLayout box = dialogBox();
        EditText etContact = addField(box, "Телефон, Telegram и т.п.", InputType.TYPE_CLASS_TEXT, user.getAdditionalContact());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Дополнительный контакт")
                .setView(box)
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Сохранить", (d, w) -> {
                    String contact = etContact.getText().toString().trim();
                    send(ApiClient.get().editContact(user.getId(), new Requests.EditContact(contact)),
                            "Контакт сохранен", "Не удалось сохранить контакт",
                            () -> {
                                user.setAdditionalContact(contact);
                                bind();
                            });
                })
                .show();
    }
    private void showChangePasswordDialog() {
        LinearLayout box = dialogBox();
        int pass = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        EditText etCurrent = addField(box, "Текущий пароль", pass, "");
        EditText etNew = addField(box, "Новый пароль (от 6 символов)", pass, "");
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Смена пароля")
                .setView(box)
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Сменить", (d, w) -> {
                    String current = etCurrent.getText().toString();
                    String next = etNew.getText().toString();
                    if (current.isEmpty() || next.length() < 6) {
                        toast("Введите текущий пароль и новый не короче 6 символов");
                        return;
                    }
                    send(ApiClient.get().editPassword(user.getId(), new Requests.EditPassword(current, next)),
                            "Пароль изменен", "Не удалось сменить пароль. Проверьте текущий пароль",
                            null);
                })
                .show();
    }
    private void send(Call<StatusResponse> call, String okText, String errorText, @Nullable Runnable onSuccess) {
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<StatusResponse> call, @NonNull Response<StatusResponse> response) {
                if (!isAdded()) return;
                StatusResponse body = response.body();
                if (response.isSuccessful() && body != null && body.isSuccess()) {
                    if (onSuccess != null) onSuccess.run();
                    toast(okText);
                } else {
                    toast(errorText);
                }
            }
            @Override
            public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) {
                if (isAdded()) toast("Нет соединения с сервером");
            }
        });
    }
    // ---------- Аватарка ----------
    private void uploadAvatar(Uri uri) {
        Context app = requireContext().getApplicationContext();
        int userId = user.getId();
        Handler main = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            try {
                ContentResolver cr = app.getContentResolver();
                String mime = cr.getType(uri);
                if (mime == null) mime = "image/jpeg";
                String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);
                if (ext == null) ext = "jpg";

                byte[] bytes = readAll(cr, uri);
                if (bytes.length > MAX_AVATAR_BYTES) {
                    main.post(() -> {
                        if (isAdded()) toast("Фото слишком большое (максимум 5 МБ)");
                    });
                    return;
                }
                RequestBody body = RequestBody.create(MediaType.parse(mime), bytes);
                MultipartBody.Part part = MultipartBody.Part.createFormData("image", "avatar." + ext, body);
                ApiClient.get().editAvatar(userId, part).enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StatusResponse> call, @NonNull Response<StatusResponse> response) {
                        if (!isAdded()) return;
                        StatusResponse b = response.body();
                        if (response.isSuccessful() && b != null && b.isSuccess()) {
                            avatarVersion = System.currentTimeMillis();
                            toast("Аватарка обновлена");
                            loadProfile();
                        } else {
                            toast("Не удалось загрузить аватарку");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) {
                        if (isAdded()) toast("Нет соединения с сервером");
                    }
                });
            } catch (IOException e) {
                main.post(() -> {
                    if (isAdded()) toast("Не удалось прочитать фото");
                });
            }
        }).start();
    }
    private byte[] readAll(ContentResolver cr, Uri uri) throws IOException {
        try (InputStream in = cr.openInputStream(uri)) {
            if (in == null) throw new IOException("no stream");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
            return out.toByteArray();
        }
    }
    private LinearLayout dialogBox() {
        LinearLayout box = new LinearLayout(requireContext());
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        box.setPadding(pad, pad / 2, pad, 0);
        return box;
    }
    private EditText addField(LinearLayout box, String hint, int inputType, String text) {
        EditText et = new EditText(requireContext());
        et.setHint(hint);
        et.setInputType(inputType);
        et.setText(text);
        et.setSingleLine(true);
        box.addView(et, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return et;
    }
    private void toast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }
}