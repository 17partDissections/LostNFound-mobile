package com.q17pd.lostfound;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddFragment extends Fragment {

    private Button btnSelectPhotos, btnSubmit;
    private EditText etDescription, etLocation;
    private ImageView ivPhotoPreview;

    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    if (ivPhotoPreview != null) {
                        ivPhotoPreview.setImageURI(uri);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        btnSelectPhotos = view.findViewById(R.id.btnSelectPhotos);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        etDescription = view.findViewById(R.id.etDescription);
        etLocation = view.findViewById(R.id.etLocation);
        ivPhotoPreview = view.findViewById(R.id.ivPhotoPreview);

        btnSelectPhotos.setOnClickListener(v -> mGetContent.launch("image/*"));

        btnSubmit.setOnClickListener(v -> {
            if (!Session.isLoggedIn(requireContext())) {
                Toast.makeText(requireContext(), "Войдите в аккаунт во вкладке «Профиль»", Toast.LENGTH_SHORT).show();
                return;
            }

            String desc = etDescription.getText().toString().trim();
            String loc = etLocation.getText().toString().trim();

            if (desc.isEmpty() || loc.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните описание и место!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Пожалуйста, выберите хотя бы одно фото", Toast.LENGTH_SHORT).show();
                return;
            }

            sendDataToServer(desc, loc, selectedImageUri);
        });

        return view;
    }

    // TODO: заменить на реальную отправку на сервер
    private void sendDataToServer(String description, String location, Uri imageUri) {
        String message = "Отправка данных:\n" +
                "Описание: " + description + "\n" +
                "Место: " + location + "\n" +
                "Фото: " + imageUri;

        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}
