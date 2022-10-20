package com.example.Android_bigWork.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.Android_bigWork.Entity.UserDish;

import java.util.List;

public class UserDishRepository {

    private UserDishDao userDishDao;
    private LiveData<List<UserDish>> userDishes;

    public UserDishRepository(Application application) {
        UserDishDatabase db=UserDishDatabase.getDatabase(application);
        userDishDao=db.userDishDao();
        userDishes= userDishDao.getUserDishesFromAllUsers();
    }

    public LiveData<List<UserDish>> getUserDishes(){
        return userDishes;
    }

    public LiveData<List<UserDish>> getUserDishesForUser(String userName){
        return userDishDao.getUserDishesForUser(userName);
    }

    public LiveData<List<UserDish>> getUserDishesForUserByTime(String userName,long time){
        return userDishDao.getUserDishesForUserByTime(userName,time);
    }

    public void insert(UserDish userDish){
        UserDishDatabase.databaseExecutor.execute(()->{
            userDishDao.insert(userDish);
        });
    }
}
