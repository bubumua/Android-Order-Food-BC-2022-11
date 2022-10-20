package com.example.Android_bigWork.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.airbnb.lottie.L;
import com.example.Android_bigWork.Database.UserDishRepository;
import com.example.Android_bigWork.Entity.UserDish;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final String TAG = "my";
    private UserDishRepository userDishRepository;
    private LiveData<List<UserDish>> userDishes;
    private LiveData<List<UserDish>> lastUserDishes;
    public MutableLiveData<Long> lastOrderCreatedTime;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        userDishRepository=new UserDishRepository(application);
        userDishes=userDishRepository.getUserDishes();
        lastOrderCreatedTime= new MutableLiveData<>(0L);
        if(userDishes.getValue()!=null&&userDishes.getValue().size()>0){
            lastOrderCreatedTime.setValue(userDishes.getValue().get(0).getCreatedTime());
        }
    }

    public LiveData<List<UserDish>> getUserDishes(){
        return userDishes;
    }

    public LiveData<List<UserDish>> getUserDishesForUser(String userName){
        userDishes=userDishRepository.getUserDishesForUser(userName);
        return userDishes;
    }

    public LiveData<List<UserDish>> getUserDishesForUserByTime(String userName,long time){
        Log.d(TAG, "getUserDishesForUserByTime: user="+userName+",time="+time);
        Log.d(TAG, "getUserDishesForUserByTime: List==null"+ (userDishRepository.getUserDishesForUserByTime(userName, time).getValue() == null));
        lastUserDishes=userDishRepository.getUserDishesForUserByTime(userName,time);
        return lastUserDishes;
    }

    public void insert(UserDish userDish){
        userDishRepository.insert(userDish);
        lastOrderCreatedTime.setValue(userDish.getCreatedTime());
    }

//    public MutableLiveData<Long> getLastOrderCreatedTime() {
//        return lastOrderCreatedTime;
//    }


}