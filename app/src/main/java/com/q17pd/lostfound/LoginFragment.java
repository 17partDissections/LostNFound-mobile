package com.q17pd.lostfound;

import android.content.Intent;
import android.os.Bundle;
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

        Intent intent = requireActivity().getIntent();
        if (intent.getBooleanExtra(AuthActivity.EXTRA_OPEN_REGISTER, false)) {
            intent.removeExtra(AuthActivity.EXTRA_OPEN_REGISTER);
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            return;
        }

        goToRegisterText.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));

        loginButton.setOnClickListener(v -> {
            String login = Objects.requireNonNull(loginEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите данные", Toast.LENGTH_SHORT).show();
            } else if (validateUser(login, password)) {
                // TODO (Андрей, Даниил): здесь будет проверка кода / запрос на сервер
                Session.login(requireContext(), login);
                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateUser(String login, String pass) {
        return login.equals("admin") && pass.equals("123456");
    }
}
