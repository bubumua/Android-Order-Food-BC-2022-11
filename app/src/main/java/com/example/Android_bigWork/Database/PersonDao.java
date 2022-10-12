package com.example.Android_bigWork.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.Android_bigWork.Entity.PersonEntity;

import java.util.List;

@Dao //Dao的声明
public interface PersonDao {
    @Query("SELECT * FROM person_table")
    List<PersonEntity> getAll();

    @Insert
    void insert(PersonEntity personEntity);

    @Delete
    void delete(PersonEntity personEntity);

    /**
     * 登录检测（使用用户名和密码的组合）
     *
     * @param username 用户名 password 密码
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("SELECT * FROM person_table WHERE username = :username and password = :password ")
    PersonEntity checkLogin(String username, String password);

    /**
     * 登录检测（使用电话号码和密码的组合）
     *
     * @param phoneNumber 电话号码 password 密码
     * @return
     * @Author Anduin9527
     * @date 2022/10/12 9:15
     * @commit
     */
    @Query("SELECT * FROM person_table WHERE phoneNumber = :phoneNumber and password = :password ")
    PersonEntity checkLoginByPhoneNumber(long phoneNumber, String password);

    /**
     * 检测用户名是否存在
     *
     * @param username 用户名
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("SELECT * FROM person_table WHERE username = :username")
    PersonEntity checkUsername(String username);

    /**
     * 检测电话号码是否存在
     *
     * @param phoneNumber 电话号码
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("SELECT * FROM person_table WHERE phoneNumber = :phoneNumber")
    PersonEntity checkPhoneNumber(long phoneNumber);

    /**
     * 修改密码
     *
     * @param username 用户名 newPassword 新密码
     * @return Update
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("UPDATE person_table SET password = :newPassword WHERE username = :username")
    void changePassword(String username, String newPassword);

}
