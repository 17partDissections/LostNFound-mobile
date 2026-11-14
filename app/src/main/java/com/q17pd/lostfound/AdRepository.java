package com.q17pd.lostfound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Временное хранилище объявлений в памяти приложения.
 * При закрытии приложения всему капут
 * TODO: когда сервер будет готов, замените search() и add() на запросы через ApiService
 */
public class AdRepository {
    private static final Locale RU = Locale.forLanguageTag("ru");
    private static final List<String> NO_PHOTOS = Collections.emptyList();
    private static final List<Ad> ads = new ArrayList<>();
    private static long nextId = 1;
    public static synchronized long generateId() {
        return nextId++;
    }
    public static synchronized void add(Ad ad) {
        ads.add(ad);
    }
    public static synchronized List<Ad> search(String query) {
        String q = query == null ? "" : query.trim().toLowerCase(RU);
        List<Ad> result = new ArrayList<>();
        for (Ad ad : ads) {
            if (q.isEmpty()
                    || ad.getDescription().toLowerCase(RU).contains(q)
                    || ad.getLocation().toLowerCase(RU).contains(q)) {
                result.add(ad);
            }
        }
        Collections.sort(result, (a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
        return result;
    }
}