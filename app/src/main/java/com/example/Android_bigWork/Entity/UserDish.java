package com.example.Android_bigWork.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Type UserDish
 * @Desc
 * @author Bubu
 * @date 2022/10/13 22:14
 */
@Entity(tableName = "order_table")
public class UserDish {
    @PrimaryKey(autoGenerate = true)
    public int OID; // 订单号
    private int GID;    // 菜品号
    private String name;    // 菜品名
    private String description; // 菜品描述
    private double price;   // 价格
    private String category;    // 分类类名
    private int CID;    // 分类编号
    private int spicy;  // 辣度
    private int sweet;  // 甜度
    private String customText;  // 客制化内容汇总
    private int count;  // 选购份数
    private String userName;    // 订单所属用户
    private long createdTime;   // 订单生成日期（时间戳）


    public UserDish(int GID, String name, String description, double price, String category, int CID, int spicy, int sweet, String customText, int count, String userName) {
        this.GID = GID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.CID = CID;
        this.spicy = spicy;
        this.sweet = sweet;
        this.customText = customText;
        this.count = count;
        this.userName = userName;
    }

    public UserDish(Dish dish, String customText) {
        this.GID = dish.getGID();
        this.name = dish.getName();
        this.description = dish.getDescription();
        this.price = dish.getPrice();
        this.category = dish.getCategory();
        this.CID = dish.getCID();
        this.customText = customText;

    }

    /**
     * 比较两个UserDish是否相等
     *
     * @param userDish
     * @return boolean
     * @Author Bubu
     * @date 2022/11/3 22:14
     * @commit
     */
    public boolean equals(UserDish userDish){
        return (userDish.getGID()==this.GID &&
                userDish.getSpicy()==this.spicy &&
                userDish.getSweet()==this.getSweet());
    }

    public String display(){
        return GID+"-"+name+"-"+price+"-"+count+"-"+userName+"--"+createdTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int GID) {
        this.GID = GID;
    }

    public int getSpicy() {
        return spicy;
    }

    public void setSpicy(int spicy) {
        this.spicy = spicy;
    }

    public int getSweet() {
        return sweet;
    }

    public void setSweet(int sweet) {
        this.sweet = sweet;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
