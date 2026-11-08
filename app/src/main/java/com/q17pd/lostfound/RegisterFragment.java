package com.q17pd.lostfound;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.q17pd.lostfound.api.ApiClient;
import com.q17pd.lostfound.api.Requests;
import com.q17pd.lostfound.api.StatusResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private Button btnRegister;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etName = view.findViewById(R.id.etRegUsername);
        TextInputEditText etSurname = view.findViewById(R.id.et_Reg_Surname);
        TextInputEditText etPhone = view.findViewById(R.id.et_Reg_Phone);
        TextInputEditText etPassword = view.findViewById(R.id.et_Reg_Password);
        btnRegister = view.findViewById(R.id.btn_register_submit);
        Button btnBackToLogin = view.findViewById(R.id.tv_back_to_login);

        btnBackToLogin.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());

        btnRegister.setOnClickListener(v -> {
            String name = Objects.requireNonNull(etName.getText()).toString().trim();
            String surname = Objects.requireNonNull(etSurname.getText()).toString().trim();
            String phone = PhoneUtils.normalize(Objects.requireNonNull(etPhone.getText()).toString());
            String password = Objects.requireNonNull(etPassword.getText()).toString();

            if (validateInputs(name, surname, phone, password)) {
                register(v, name, surname, phone, password);
            }
        });
    }

    private void register(View view, String name, String surname, String phone, String password) {
        btnRegister.setEnabled(false);

        ApiClient.get().register(new Requests.Register(name, surname, phone, password))
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StatusResponse> call, @NonNull Response<StatusResponse> response) {
                        if (!isAdded()) return;
                        btnRegister.setEnabled(true);

                        StatusResponse body = response.body();
                        if (response.isSuccessful() && body != null && body.isSuccess()) {
                            showMessage("Регистрация успешна! Теперь войдите.");
                            Navigation.findNavController(view).popBackStack();
                        } else {
                            showMessage("Не удалось зарегистрироваться, попробуйте позже");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        btnRegister.setEnabled(true);
                        showMessage("Нет соединения с сервером");
                    }
                });
    }
    private boolean validateInputs(String name, String surname, String phone, String pass) {
        if (name.isEmpty()) {
            showMessage("Введите имя");
            return false;
        }
        if (surname.isEmpty()) {
            showMessage("Введите фамилию");
            return false;
        }
        if (phone.length() != 11) {
            showMessage("Введите номер полностью, например +7 900 123-45-67");
            return false;
        }
        if (pass.length() < 6) {
            showMessage("Пароль должен быть не менее 6 символов");
            return false;
        }
        return true;
    }
    private void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}