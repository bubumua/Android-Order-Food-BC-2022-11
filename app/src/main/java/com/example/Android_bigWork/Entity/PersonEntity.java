package com.example.Android_bigWork.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "person_table")//实体类的声明
public class PersonEntity {
    @PrimaryKey(autoGenerate = true)
    public int UID;

    public String username;
    public String password;
    public long phoneNumber;
    public int gender;

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    public PersonEntity(String username, String password, long phoneNumber, int gender) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "PersonEntity{" +
                "UID=" + UID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", gender=" + gender +
                '}';
    }
}
