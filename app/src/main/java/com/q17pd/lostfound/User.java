package com.q17pd.lostfound;

import com.google.gson.annotations.SerializedName;
import com.q17pd.lostfound.api.ApiClient;

public class User {

    private int id;
    private String name;
    private String surname;
    @SerializedName("phone_num")
    private String phoneNum;
    @SerializedName("additional_contact")
    private String additionalContact;
    private String avatar;

    public int getId() { return id; }
    public String getName() { return name == null ? "" : name; }
    public String getSurname() { return surname == null ? "" : surname; }
    public String getPhoneNum() { return phoneNum == null ? "" : phoneNum; }
    public String getAdditionalContact() { return additionalContact == null ? "" : additionalContact; }

    public String getFullName() {
        return (getName() + " " + getSurname()).trim();
    }

    public String getAvatarUrl() {
        if (avatar == null || avatar.isEmpty()) return null;
        if (avatar.startsWith("http")) return avatar;
        return ApiClient.BASE_URL + avatar;
    }

    public void setFullName(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setAdditionalContact(String additionalContact) {
        this.additionalContact = additionalContact;
    }
}