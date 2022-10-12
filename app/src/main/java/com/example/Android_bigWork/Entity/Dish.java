package com.example.Android_bigWork.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dish_table")//实体类的声明
public class Dish {
    @PrimaryKey
    private int GID;
    private String name;
    private String description;
    private double price;
    private String category;
    private int CID;
    private boolean customizable;

    public Dish() {

    }

    public Dish(int gid, String name, String description, double price, String category, int CID, boolean customizable) {
        this.GID = gid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.CID = CID;
        this.customizable = customizable;
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
        return "Dish{" + "GID=" + GID + ", name='" + name + '\'' + ", description='" + description + '\'' + ", price=" + price + ", category='" + category + '\'' + ", CID=" + CID + ", customizable=" + customizable + '}';
    }
}
