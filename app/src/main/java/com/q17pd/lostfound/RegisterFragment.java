package com.q17pd.lostfound;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etName = view.findViewById(R.id.et_Reg_Login);
        EditText etEmail = view.findViewById(R.id.et_Reg_Email);
        EditText etPassword = view.findViewById(R.id.et_Reg_Password);
        Button btnRegister = view.findViewById(R.id.btn_register_submit);
        TextView tvBackToLogin = view.findViewById(R.id.tv_back_to_login);

        tvBackToLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();

        });

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInputs(name, email, password)) {
                Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();

                Navigation.findNavController(v).popBackStack();
            }
        });
    }

    private boolean validateInputs(String name, String email, String pass) {
        if (name.isEmpty()) {
            showError("Введите имя");
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