package com.q17pd.lostfound;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etName = view.findViewById(R.id.etRegUsername);
        TextInputEditText etLogin = view.findViewById(R.id.et_Reg_Login);
        TextInputEditText etEmail = view.findViewById(R.id.et_Reg_Email);
        TextInputEditText etPassword = view.findViewById(R.id.et_Reg_Password);
        Button btnRegister = view.findViewById(R.id.btn_register_submit);
        Button btnBackToLogin = view.findViewById(R.id.tv_back_to_login);

        btnBackToLogin.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());

        btnRegister.setOnClickListener(v -> {
            String name = Objects.requireNonNull(etName.getText()).toString().trim();
            String login = Objects.requireNonNull(etLogin.getText()).toString().trim();
            String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if (validateInputs(name, login, email, password)) {
                // TODO: отправить данные регистрации на сервер
                Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).popBackStack();
            }
        });
    }

    private boolean validateInputs(String name, String login, String email, String pass) {
        if (name.isEmpty()) {
            showError("Введите имя");
            return false;
        }
        if (login.isEmpty()) {
            showError("Введите логин");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректный Email");
            return false;
        }
        if (pass.length() < 6) {
            showError("Пароль должен быть не менее 6 символов");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
