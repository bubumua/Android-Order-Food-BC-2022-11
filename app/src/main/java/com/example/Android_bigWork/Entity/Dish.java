package com.example.Android_bigWork.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Bubu
 * @Type Dish
 * @Desc 菜单上的菜品
 * @date 2022/10/13 20:11
 */
@Entity(tableName = "dish_table")
public class Dish {     // 菜品类
    @PrimaryKey
    private int GID;    // 菜品在菜单上的唯一编号
    private String name;    // 菜品名
    private String description; // 菜品描述
    private double price;   // 价格(单价)
    private String category;    // 分类类名
    private int CID;    // 分类编号
    private boolean customizable;   // 是否支持定制需求
    private boolean spicy;  // 辣味是否可选
    private boolean sweet;  // 甜味是否可选
    private int count;  // 选购份数

    public Dish() {

    }

    public Dish(int gid, String name, String description, double price, String category, int CID, boolean spicy, boolean sweet) {
        this.GID = gid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.CID = CID;
        this.spicy = spicy;
        this.sweet = sweet;
        this.count = 0;
    }

    public boolean isSpicy() {
        return spicy;
    }

    public void setSpicy(boolean spicy) {
        this.spicy = spicy;
    }

    public boolean isSweet() {
        return sweet;
    }

    public void setSweet(boolean sweet) {
        this.sweet = sweet;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int GID) {
        this.GID = GID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "GID=" + GID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", CID=" + CID +
                ", customizable=" + customizable +
                ", spicy=" + spicy +
                ", sweet=" + sweet +
                ", count=" + count +
                '}';
    }
}
