package com.q17pd.lostfound.api;

import com.google.gson.annotations.SerializedName;

public class Requests {

    /** POST /users */
    public static class Register {
        @SerializedName("name") private final String name;
        @SerializedName("surname") private final String surname;
        @SerializedName("phone_num") private final String phoneNum;
        @SerializedName("passwd") private final String passwd;
        public Register(String name, String surname, String phoneNum, String passwd) {
            this.name = name;
            this.surname = surname;
            this.phoneNum = phoneNum;
            this.passwd = passwd;
        }
    }
    public static class EditName {
        @SerializedName("name") private final String name;
        @SerializedName("surname") private final String surname;

        public EditName(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
    }
    public static class EditContact {
        @SerializedName("additional_contact") private final String additionalContact;

        public EditContact(String additionalContact) {
            this.additionalContact = additionalContact;
        }
    }
    public static class EditPassword {
        @SerializedName("current_passwd") private final String currentPasswd;
        @SerializedName("new_passwd") private final String newPasswd;

        public EditPassword(String currentPasswd, String newPasswd) {
            this.currentPasswd = currentPasswd;
            this.newPasswd = newPasswd;
        }
    }
}