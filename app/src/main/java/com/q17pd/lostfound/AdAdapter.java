package com.q17pd.lostfound;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class AdAdapter extends RecyclerView.Adapter<AdAdapter.Holder> {
    public interface Listener {
        void onContact(Ad ad);
        void onPhotos(Ad ad);
    }
    private final List<Ad> items = new ArrayList<>();
    private final Listener listener;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("d MMMM, HH:mm", Locale.forLanguageTag("ru"));
    public AdAdapter(Listener listener) {
        this.listener = listener;
    }
    public void submit(List<Ad> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
        return new Holder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        Ad ad = items.get(position);
        h.description.setText(ad.getDescription());
        h.location.setText(ad.getLocation());
        h.date.setText(dateFormat.format(new Date(ad.getCreatedAt())));
        String cover = ad.getCoverUri();
        boolean hasPhotos = cover != null;
        if (hasPhotos) {
            h.photo.setVisibility(View.VISIBLE);
            h.placeholder.setVisibility(View.GONE);
            Glide.with(h.photo).load(Uri.parse(cover)).centerCrop().into(h.photo);
            h.photoCount.setVisibility(View.VISIBLE);
            h.photoCount.setText(ad.getPhotos().size() + " фото");
        } else {
            Glide.with(h.photo).clear(h.photo);
            h.photo.setVisibility(View.GONE);
            h.placeholder.setVisibility(View.VISIBLE);
            h.photoCount.setVisibility(View.GONE);
        }
        h.photoFrame.setOnClickListener(v -> listener.onPhotos(ad));
        h.photoFrame.setClickable(hasPhotos);
        h.contact.setOnClickListener(v -> listener.onContact(ad));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    static class Holder extends RecyclerView.ViewHolder {
        final View photoFrame;
        final ImageView photo, placeholder;
        final TextView description, location, date, photoCount;
        final Button contact;
        Holder(@NonNull View v) {
            super(v);
            photoFrame = v.findViewById(R.id.flAdPhoto);
            photo = v.findViewById(R.id.ivAdPhoto);
            placeholder = v.findViewById(R.id.ivAdPlaceholder);
            photoCount = v.findViewById(R.id.tvAdPhotoCount);
            description = v.findViewById(R.id.tvAdDescription);
            location = v.findViewById(R.id.tvAdLocation);
            date = v.findViewById(R.id.tvAdDate);
            contact = v.findViewById(R.id.btnAdContact);
        }
    }
}