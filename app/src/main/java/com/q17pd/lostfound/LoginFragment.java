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
import com.q17pd.lostfound.api.ApiClient;
import com.q17pd.lostfound.api.LoginRequest;
import com.q17pd.lostfound.api.LoginResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private Button loginButton;
    public LoginFragment() {
        super(R.layout.fragment_login);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText phoneEditText = view.findViewById(R.id.login_input);
        TextInputEditText passwordEditText = view.findViewById(R.id.password_input);
        loginButton = view.findViewById(R.id.btn_login_submit);
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
            String phone = PhoneUtils.normalize(Objects.requireNonNull(phoneEditText.getText()).toString());
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();
            if (phone.isEmpty() || password.isEmpty()) {
                toast("Введите номер телефона и пароль");
                return;
            }
            if (phone.length() != 11) {
                toast("Введите номер полностью, например +7 900 123-45-67");
                return;
            }
            login(phone, password);
        });
    }
    private void login(String phone, String password) {
        loginButton.setEnabled(false);
        ApiClient.get().login(new LoginRequest(phone, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (!isAdded()) return;
                LoginResponse body = response.body();

                if (response.isSuccessful() && body != null && body.isSuccess()) {
                    loadProfile(phone);
                } else {
                    loginButton.setEnabled(true);
                    if (response.code() >= 500) {
                        toast("Ошибка сервера, попробуйте позже");
                    } else {
                        toast("Неверный номер или пароль");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                loginButton.setEnabled(true);
                toast("Нет соединения с сервером");
            }
        });
    }
    private void loadProfile(String phone) {
        ApiClient.get().getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (!isAdded()) return;
                loginButton.setEnabled(true);
                User me = null;
                List<User> users = response.body();
                if (response.isSuccessful() && users != null) {
                    for (User u : users) {
                        if (phone.equals(PhoneUtils.normalize(u.getPhoneNum()))) {
                            me = u;
                            break;
                        }
                    }
                }
                if (me != null) {
                    Session.login(requireContext(), me.getId(), phone);
                    requireActivity().finish();
                } else {
                    toast("Не удалось загрузить профиль");
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                loginButton.setEnabled(true);
                toast("Нет соединения с сервером");
            }
        });
    }
    private void toast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }
}