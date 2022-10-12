package com.example.Android_bigWork.Entity;

import androidx.room.PrimaryKey;

public class UserDish {
    @PrimaryKey
    /*菜品在菜单上的唯一编号，共5位，从左往右数
    * 第一位：分类编号
    * 第二、三位：原菜品名，即菜品在菜单上的唯一编号
    * 第四、五位：0表示不可定制，大于0表示定制程度
    * */
    private int GID;
    private String name;    // 菜品名
    private String description; // 菜品描述
    private double price;   // 价格
    private String category;    // 分类类名
    private int CID;    // 分类编号
    private boolean spicy;  // 辣味是否可选
    private boolean sweet;  // 甜味是否可选
    private int count;  // 选购份数
    private int UID;    // 订单所属用户
    private String date;    // 订单生成日期

    public UserDish() {

    }
}
