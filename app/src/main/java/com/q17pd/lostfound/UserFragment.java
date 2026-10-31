package com.q17pd.lostfound;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class UserFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PHONE = "phone";
    private static final String ARG_AVATAR = "avatar";

    public UserFragment() {
    }

    public static UserFragment newInstance(User user) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, user.getUserId());
        args.putString(ARG_NAME, user.getName());
        args.putString(ARG_EMAIL, user.getEmail());
        args.putString(ARG_PHONE, user.getPhone());
        args.putString(ARG_AVATAR, user.getAvatarUrl());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        TextView tvName = view.findViewById(R.id.tvUserName);
        TextView tvEmail = view.findViewById(R.id.tvUserEmail);
        TextView tvPhone = view.findViewById(R.id.tvUserPhone);
        ShapeableImageView userAvatar = view.findViewById(R.id.ivUserAvatar);

        Bundle args = getArguments();
        if (args != null) {
            tvName.setText(args.getString(ARG_NAME));
            tvEmail.setText(args.getString(ARG_EMAIL));
            tvPhone.setText(args.getString(ARG_PHONE));

            // АВАТАРКИ:подгрузка с сервера или из сети!!!!
            String avatarUrl = args.getString(ARG_AVATAR);

            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Glide.with(requireContext())
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_person_outline)
                        .error(R.drawable.ic_person_error)
                        .into(userAvatar);
            } else {
                userAvatar.setImageResource(R.drawable.ic_person_outline);
            }
        }

        return view;
    }
}
