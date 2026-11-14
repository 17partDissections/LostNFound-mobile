package com.q17pd.lostfound;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
// диалоговое окно Альбома с фото(Мне не понравилось что можно одно фото я запарился и теперь можно 5 или более)
public class PhotoAlbumDialog extends DialogFragment {
    private static final String ARG_PHOTOS = "photos";
    private static final String ARG_POSITION = "position";
    private static final String STATE_CURRENT = "current";
    private List<String> photos;
    private int current;
    private TextView tvCounter, tvCaption;
    public static PhotoAlbumDialog newInstance(List<String> photos, int position) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PHOTOS, new ArrayList<>(photos));
        args.putInt(ARG_POSITION, position);
        PhotoAlbumDialog dialog = new PhotoAlbumDialog();
        dialog.setArguments(args);
        return dialog;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_LostFound);
        ArrayList<String> list = requireArguments().getStringArrayList(ARG_PHOTOS);
        photos = list == null ? new ArrayList<>() : list;
        current = savedInstanceState != null
                ? savedInstanceState.getInt(STATE_CURRENT)
                : requireArguments().getInt(ARG_POSITION, 0);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo_album, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        tvCounter = view.findViewById(R.id.tvAlbumCounter);
        tvCaption = view.findViewById(R.id.tvAlbumCaption);
        view.findViewById(R.id.btnAlbumClose).setOnClickListener(v -> dismiss());
        RecyclerView rv = view.findViewById(R.id.rvAlbum);
        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);
        rv.setAdapter(new PagerAdapter(photos));
        PagerSnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(rv);
        lm.scrollToPosition(current);
        updateInfo();
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snapped = snap.findSnapView(lm);
                    if (snapped != null) {
                        current = lm.getPosition(snapped);
                        updateInfo();
                    }
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            WindowCompat.getInsetsController(window, window.getDecorView()).setAppearanceLightStatusBars(false);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT, current);
    }
    private void updateInfo() {
        tvCounter.setText((current + 1) + " / " + photos.size());
        tvCaption.setText(Ad.photoLabel(current));
    }
    private static class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.Holder> {
        private final List<String> items;
        PagerAdapter(List<String> items) {
            this.items = items;
        }
        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_photo, parent, false);
            return new Holder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull Holder h, int position) {
            Glide.with(h.image).load(Uri.parse(items.get(position))).into(h.image);
        }
        @Override
        public int getItemCount() {
            return items.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final ImageView image;
            Holder(@NonNull View v) {
                super(v);
                image = (ImageView) v;
            }
        }
    }
}