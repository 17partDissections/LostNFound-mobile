package com.q17pd.lostfound;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText loginEditText = view.findViewById(R.id.login_input);
        TextInputEditText passwordEditText = view.findViewById(R.id.password_input);
        Button loginButton = view.findViewById(R.id.btn_login_submit);
        TextView goToRegisterText = view.findViewById(R.id.tv_go_to_register);

        goToRegisterText.setOnClickListener(v -> {
        });

        loginButton.setOnClickListener(v -> {
            String login = Objects.requireNonNull(loginEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите данные", Toast.LENGTH_SHORT).show();
            } else {
                if (validateUser(login, password)) {
                    startVerificationProcess(login);
                } else {
                    Toast.makeText(requireContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateUser(String login, String pass) {
        return login.equals("admin") && pass.equals("123456");
    }

    private void startVerificationProcess(String email) {
        Toast.makeText(requireContext(), "Код отправлен на " + email, Toast.LENGTH_LONG).show();

    }
}