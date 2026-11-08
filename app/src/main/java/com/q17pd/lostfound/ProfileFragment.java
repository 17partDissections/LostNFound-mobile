package com.q17pd.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private LinearLayout layoutGuest;
    private LinearLayout layoutUser;
    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutGuest = view.findViewById(R.id.layoutGuest);
        layoutUser = view.findViewById(R.id.layoutUser);
        Button btnSignIn = view.findViewById(R.id.btnSignIn);
        Button btnSignUp = view.findViewById(R.id.btnSignUp);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnSignIn.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AuthActivity.class)));
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.putExtra(AuthActivity.EXTRA_OPEN_REGISTER, true);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            Session.logout(requireContext());
            updateUi();
            Toast.makeText(requireContext(), "Вы вышли из системы", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }
    private void updateUi() {
        boolean logged = Session.isLoggedIn(requireContext());
        layoutGuest.setVisibility(logged ? View.GONE : View.VISIBLE);
        layoutUser.setVisibility(logged ? View.VISIBLE : View.GONE);
    }
}