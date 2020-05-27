package com.example.maskday;

import io.realm.RealmObject;

public class UserModel extends RealmObject {
    private int birthYear;

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
