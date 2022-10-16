package com.example.Android_bigWork.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.Android_bigWork.Entity.Dish;

import java.util.List;

public class DishMenu extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private LiveData<List<Dish>> allDishes;

    public DishMenu(@NonNull Application application) {
        super(application);
    }

}