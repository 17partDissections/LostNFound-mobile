package com.q17pd.lostfound;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;
public class FeedFragment extends Fragment {
    private AdAdapter adapter;
    private TextView tvEmpty;
    private String query = "";
    public FeedFragment() {
        super(R.layout.fragment_feed);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFeed);
        tvEmpty = view.findViewById(R.id.tvFeedEmpty);
        EditText etSearch = view.findViewById(R.id.etSearch);

        adapter = new AdAdapter(new AdAdapter.Listener() {
            @Override
            public void onContact(Ad ad) {
                showContact(ad);
            }
            @Override
            public void onPhotos(Ad ad) {
                if (!ad.getPhotos().isEmpty()) {
                    PhotoAlbumDialog.newInstance(ad.getPhotos(), 0)
                            .show(getChildFragmentManager(), "album");
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();
                refresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
    private void refresh() {
        List<Ad> list = AdRepository.search(query);
        adapter.submit(list);

        boolean empty = list.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        tvEmpty.setText(query.trim().isEmpty()
                ? "Пока нет объявлений.\nНашли вещь? Добавьте её на вкладке «+»."
                : "Ничего не найдено.\nПопробуйте другое слово.");
    }
    private void showContact(Ad ad) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Связаться с нашедшим")
                .setMessage("Контакт: " + ad.getContact() + "\nГде найдено: " + ad.getLocation())
                .setNegativeButton("Закрыть", null)
                .setPositiveButton("Скопировать контакт", (d, w) -> {
                    ClipboardManager clipboard = (ClipboardManager)
                            requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(ClipData.newPlainText("contact", ad.getContact()));
                    Toast.makeText(requireContext(), "Контакт скопирован", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}