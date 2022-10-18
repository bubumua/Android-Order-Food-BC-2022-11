package com.example.Android_bigWork.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.Android_bigWork.Database.UserDishRepository;
import com.example.Android_bigWork.Entity.UserDish;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final String TAG = "my";
    private UserDishRepository userDishRepository;
    private LiveData<List<UserDish>> userDishes;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        userDishRepository=new UserDishRepository(application);
        userDishes=userDishRepository.getUserDishes();
    }

    public LiveData<List<UserDish>> getUserDishes(){
        return userDishes;
    }

    public LiveData<List<UserDish>> getUserDishesForUser(String userName){
        userDishes=userDishRepository.getUserDishesForUser(userName);
        return userDishes;
    }

    public void insert(UserDish userDish){
        userDishRepository.insert(userDish);
    }
}