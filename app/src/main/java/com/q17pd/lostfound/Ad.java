package com.q17pd.lostfound;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Ad {
    private final long id;
    private final String description;
    private final String location;
    private final List<String> photos;
    private final String contact;
    private final long createdAt;
    public Ad(long id, String description, String location, List<String> photos, String contact, long createdAt) {
        this.id = id;
        this.description = description;
        this.location = location;
        this.photos = Collections.unmodifiableList(new ArrayList<>(photos));
        this.contact = contact;
        this.createdAt = createdAt;
    }
    public long getId() { return id; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public List<String> getPhotos() { return photos; }
    public String getContact() { return contact; }
    public long getCreatedAt() { return createdAt; }
    public String getCoverUri() {
        return photos.isEmpty() ? null : photos.get(0);
    }
    public static String photoLabel(int index) {
        return "Фото " + (index + 1);
    }
}