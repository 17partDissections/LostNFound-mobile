package com.q17pd.lostfound;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
//Нужно минимум 3 фото ( спереди, сзади и сбоку). Остальные фото необязательные
public class AddFragment extends Fragment {
    private static final int MIN_PHOTOS = 3;
    private static final int MAX_PHOTOS = 5;
    private static final String KEY_PHOTOS = "photos";
    private final List<Uri> photoUris = new ArrayList<>();
    private LinearLayout llPhotos;
    private TextView tvPhotoStatus;
    private Button btnAddPhoto;
    private EditText etDescription, etLocation;
    private final ActivityResultLauncher<String> pickPhotos = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(),
            uris -> {
                if (uris == null || uris.isEmpty()) return;
                for (Uri u : uris) {
                    if (photoUris.size() >= MAX_PHOTOS) {
                        toast("Можно добавить не больше " + MAX_PHOTOS + " фото");
                        break;
                    }
                    if (!photoUris.contains(u)) photoUris.add(u);
                }
                renderPhotos();
            }
    );
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        etDescription = view.findViewById(R.id.etDescription);
        etLocation = view.findViewById(R.id.etLocation);
        llPhotos = view.findViewById(R.id.llPhotos);
        tvPhotoStatus = view.findViewById(R.id.tvPhotoStatus);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        btnAddPhoto.setOnClickListener(v -> pickPhotos.launch("image/*"));
        btnSubmit.setOnClickListener(v -> onSubmit());

        if (savedInstanceState != null) {
            ArrayList<String> saved = savedInstanceState.getStringArrayList(KEY_PHOTOS);
            if (saved != null) {
                for (String s : saved) photoUris.add(Uri.parse(s));
            }
        }
        renderPhotos();
        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> list = new ArrayList<>();
        for (Uri u : photoUris) list.add(u.toString());
        outState.putStringArrayList(KEY_PHOTOS, list);
    }
    private void renderPhotos() {
        llPhotos.removeAllViews();
        for (int i = 0; i < photoUris.size(); i++) {
            View thumb = LayoutInflater.from(requireContext()).inflate(R.layout.item_photo_thumb, llPhotos, false);
            ImageView iv = thumb.findViewById(R.id.ivThumb);
            Glide.with(iv).load(photoUris.get(i)).centerCrop().into(iv);

            final int index = i;
            thumb.findViewById(R.id.btnThumbRemove).setOnClickListener(v -> {
                photoUris.remove(index);
                renderPhotos();
            });
            llPhotos.addView(thumb);
        }
        llPhotos.setVisibility(photoUris.isEmpty() ? View.GONE : View.VISIBLE);
        int n = photoUris.size();
        String status = n >= MIN_PHOTOS
                ? "Фото: " + n + " из " + MAX_PHOTOS + ". Минимум набран"
                : "Фото: " + n + " из " + MAX_PHOTOS + ". Нужно ещё минимум " + (MIN_PHOTOS - n);
        tvPhotoStatus.setText(status);
        btnAddPhoto.setEnabled(n < MAX_PHOTOS);
    }
    private void onSubmit() {
        if (!Session.isLoggedIn(requireContext())) {
            toast("Войдите в аккаунт во вкладке «Профиль»");
            return;
        }
        String desc = etDescription.getText().toString().trim();
        String loc = etLocation.getText().toString().trim();
        if (desc.isEmpty() || loc.isEmpty()) {
            toast("Заполните описание и место!");
            return;
        }
        if (photoUris.size() < MIN_PHOTOS) {
            toast("Добавьте минимум " + MIN_PHOTOS + " фото");
            return;
        }
        publish(desc, loc);
    }
    /**
     * Пока объявление сохраняется только в памяти приложения.
     * TODO: когда сервер будет готов, отправить его через ApiService вместо AdRepository.add().
     */
    private void publish(String description, String location) {
        List<String> photos = new ArrayList<>();
        for (Uri u : photoUris) photos.add(u.toString());
        String contact = "+" + Session.getPhone(requireContext());
        AdRepository.add(new Ad(
                AdRepository.generateId(),
                description,
                location,
                photos,
                contact,
                System.currentTimeMillis()));
        etDescription.setText("");
        etLocation.setText("");
        photoUris.clear();
        renderPhotos();

        toast("Объявление опубликовано");

        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).openFeed();
        }
    }
    private void toast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }
}