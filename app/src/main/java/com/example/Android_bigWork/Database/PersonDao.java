package com.example.Android_bigWork.Database;

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
    void insert(PersonEntity personEntity);

    @Delete
    void delete(PersonEntity personEntity);

    //使用用户名和密码进行登陆检查
    @Query("SELECT * FROM person_table WHERE username = :username and password = :password ")
    PersonEntity checkLogin(String username, String password);

    //使用手机号和密码进行登录检查
    @Query("SELECT * FROM person_table WHERE phoneNumber = :phoneNumber and password = :password ")
    PersonEntity checkLoginByPhoneNumber(long phoneNumber, String password);

    //查询是否存在该用户名
    @Query("SELECT * FROM person_table WHERE username = :username")
    PersonEntity checkUsername(String username);

    //查询是否存在该手机号
    @Query("SELECT * FROM person_table WHERE phoneNumber = :phoneNumber")
    PersonEntity checkPhoneNumber(long phoneNumber);

}
