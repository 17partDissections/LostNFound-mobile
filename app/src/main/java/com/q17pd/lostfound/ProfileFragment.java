package com.q17pd.lostfound;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private LinearLayout layoutGuest;
    private LinearLayout layoutUser;
    private TextView tvUserName;
    private Button btnSignIn;
    private Button btnSignUp;
    private Button btnLogout;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupViews(view);

        setupListeners();

        showUserMode("Имя пользователя");

        return view;
    }

    private void setupViews(View view) {
        layoutGuest = view.findViewById(R.id.layoutGuest);
        layoutUser = view.findViewById(R.id.layoutUser);
        tvUserName = view.findViewById(R.id.tvUserName);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnSignIn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Переход на экран входа...", Toast.LENGTH_SHORT).show();
        });

        btnSignUp.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Переход на регистрацию...", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            showGuestMode();
            Toast.makeText(getContext(), "Вы вышли из системы", Toast.LENGTH_SHORT).show();
        });
    }

    private void showGuestMode() {
        layoutGuest.setVisibility(View.VISIBLE);
        layoutUser.setVisibility(View.GONE);
    }

    private void showUserMode(String name) {
        layoutGuest.setVisibility(View.GONE);
        layoutUser.setVisibility(View.VISIBLE);
        if (tvUserName != null) {
            tvUserName.setText(name);
        }
    }
}