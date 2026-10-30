package com.q17pd.lostfound;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private User userData;

    public UserFragment(User user) {
        this.userData = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView tvName = view.findViewById(R.id.tvUserName);
        TextView tvEmail = view.findViewById(R.id.tvUserEmail);
        TextView tvPhone = view.findViewById(R.id.tvUserPhone);

        if (userData != null) {
            tvName.setText(userData.getName());
            tvEmail.setText(userData.getEmail());
            tvPhone.setText(userData.getPhone());
        }

        return view;
    }
}