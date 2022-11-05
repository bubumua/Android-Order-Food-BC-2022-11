package com.example.Android_bigWork.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Android_bigWork.Entity.Dish;

import java.util.ArrayList;
import java.util.List;

@Dao //Dao的声明
public interface DishDao {
    /**
     * 获取所有菜品
     *
     * @param
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("SELECT * FROM dish_table ORDER BY CID")
    List<Dish> getAllDish();

    /**
     * 清除所有菜品
     *
     * @param
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("DELETE FROM dish_table")
    void deleteAllDish();

    /**
     * 添加菜品
     *
     * @param dish
     * @return void
     * @Author Bubu
     * @date 2022/10/15 21:51
     * @commit
     */
    @Insert
    void insert(Dish dish);

    @Update
    void update(Dish dish);

    @Delete
    void delete(Dish Dish);

    /**
     * 返回菜品信息
     *
     * @param name 菜品名
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:14
     * @commit
     */
    @Query("SELECT * FROM dish_table WHERE name = :name")
    Dish getDishByName(String name);

    /**
     * 返回该分类下所有菜品信息
     *
     * @param category 菜品分类
     * @return Query
     * @Author Anduin9527
     * @date 2022/10/12 9:47
     * @commit
     */
    @Query("SELECT * FROM dish_table WHERE category = :category")
    List<Dish> getDishByCategory(String category);

    @Query("SELECT COUNT(*) FROM dish_table")
    int getDishCount();

}
