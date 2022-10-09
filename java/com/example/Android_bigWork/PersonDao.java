package com.example.Android_bigWork;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao //Dao的声明
public interface PersonDao {
    @Query("SELECT * FROM person_table")
    List<PersonEntity> getAll();

    @Insert
    void insert(PersonEntity... personEntities);

    @Delete
    void delete(PersonEntity personEntity);

    @Query("SELECT * FROM person_table WHERE username = :username and password = :password")
    PersonEntity checkLogin(String username, String password);


}
