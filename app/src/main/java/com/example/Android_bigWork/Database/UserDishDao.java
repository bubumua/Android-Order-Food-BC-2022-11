package com.example.Android_bigWork.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.Android_bigWork.Entity.UserDish;

import java.util.List;

@Dao
public interface UserDishDao {
    @Insert
    void insert(UserDish userDish);

    @Query("SELECT * FROM order_table WHERE userName= :userName ORDER BY createdTime DESC")
    LiveData<List<UserDish>> getUserDishesForUser(String userName);

    @Query("SELECT * FROM order_table ORDER BY createdTime DESC")
    LiveData<List<UserDish>> getUserDishesFromAllUsers();

    @Query("SELECT * FROM order_table WHERE userName= :userName AND createdTime= :time")
    LiveData<List<UserDish>> getUserDishesForUserByTime(String userName,long time);
}
