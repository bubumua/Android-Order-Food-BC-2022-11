package com.example.Android_bigWork.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.Android_bigWork.Database.CouponDao;

/**
 * @author Anduin9527
 * @Type Coupon
 * @Desc
 * @date 2022/10/18 19:16
 */
@Entity(tableName = "coupon_table")
public class Coupon {


    @PrimaryKey(autoGenerate = true)
    public int CID;
    private int type;
    public static final int DISCOUNT = 0;
    public static final int FULL_REDUCTION = 1;
    private double discount;
    private double condition;
    private double reduction;
    private String username;

    public Coupon(int type, double discount, double condition, double reduction, String username) {
        this.type = type;
        this.discount = discount;
        this.condition = condition;
        this.reduction = reduction;
        this.username = username;
    }

//    public Coupon() {
//    }

    @Override
    public String toString() {
        return "Coupon{" +
                "CID=" + CID +
                ", type=" + type +
                ", discount=" + discount +
                ", condition=" + condition +
                ", reduction=" + reduction +
                ", username='" + username +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getCondition() {
        return condition;
    }

    public void setCondition(double condition) {
        this.condition = condition;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }
}
