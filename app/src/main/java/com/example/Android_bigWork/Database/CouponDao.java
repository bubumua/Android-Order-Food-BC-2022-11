package com.example.Android_bigWork.Database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.Android_bigWork.Entity.Coupon;

import java.util.List;
@Dao
public interface CouponDao {
    //获取用户的所有优惠券
    @Query("SELECT * FROM coupon_table WHERE username = :username")
    List<Coupon> getAllCoupon(String username);

    //给用户添加优惠券，传入优惠券的类型和参数
    @Query("INSERT INTO coupon_table (username, type, discount, condition,reduction) VALUES (:username, :type, :discount, :condition, :reduction)")
    void addCoupon(String username, int type, double discount, double condition, double reduction);

    //删除用户的优惠券
    @Query("DELETE FROM coupon_table WHERE CID = :CID")
    void deleteCoupon(int CID);
}
