package com.example.Android_bigWork.Utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.example.Android_bigWork.Entity.Coupon;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bubu
 * @Type StringUtil
 * @Desc 处理字符串
 * @date 2022/10/23 22:05
 */
public class StringUtil {
    /**
     * 将字符串中的特殊符号删除
     *
     * @param str 待处理的字符串
     * @return String
     * @Author Bubu
     * @date 2022/10/26 18:38
     * @commit
     */
    public static String replaceToBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 根据给定金额，符号大小，红包，计算经过红包折扣后的金额字符串样式
     *
     * @param moneyBeforeDiscount 折扣前的金额
     * @param symbolSize          金额前￥符号大小
     * @param coupon              进行折扣的红包
     * @return android.text.SpannableString
     * @Author Bubu
     * @date 2022/11/3 22:08
     * @commit
     */
    public static SpannableString getSSMoneyAfterDiscount(double moneyBeforeDiscount, int symbolSize, Coupon coupon) {
        String moneyBefore = String.valueOf(moneyBeforeDiscount);
        int lengthBefore = moneyBefore.length();
        double moneyAfterDiscount = moneyBeforeDiscount;
        // 根据红包类型的不同，进行不同的计算
        switch (coupon.getType()) {
            case 0:
                moneyAfterDiscount = coupon.getDiscount() * moneyBeforeDiscount / 10;
                break;
            case 1:
                if (moneyBeforeDiscount >= coupon.getCondition()) {
                    moneyAfterDiscount -= coupon.getReduction();
                }
            default:
                break;
        }
        String moneyAfter = String.valueOf(moneyAfterDiscount);
        int lengthAfter = moneyAfter.length();
        SpannableString ss = new SpannableString("￥" + moneyBefore + moneyAfter);
        // 设置大小
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(symbolSize);
        // 加粗
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        // 设置颜色
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#ffc107"));
        // 下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();
        // 删除线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        // 将上述设置应用到SpannableString, 并设置应用范围
        ss.setSpan(yellowSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(strikethroughSpan, 1, 1 + lengthBefore, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 获取显示在购物车中的总金额
     *
     * @param money      金额
     * @param symbolSize 金钱符号大小
     * @return SpannableString
     * @Author Bubu
     * @date 2022/10/26 18:38
     * @commit
     */
    public static SpannableString getSSMoney(double money, int symbolSize) {
        String moneyStr = String.valueOf(money);
        SpannableString ss = new SpannableString("￥" + moneyStr);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(symbolSize);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);//加粗
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ffc107")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    /**
     * 将List转化为字符串
     *
     * @param list 待转换的List
     * @param div  分隔符
     * @return String
     * @Author Bubu
     * @date 2022/10/26 18:40
     * @commit
     */
    public static String join(List<String> list, String div) {
        StringBuilder s = new StringBuilder();
        if (list.size() == 0) {
            return s.toString();
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    s.append(list.get(i));
                } else {
                    s.append(div).append(list.get(i));
                }
            }
            return s.toString();
        }
    }

    /**
     * 获取当前系统的日期与时间
     *
     * @return java.lang.String
     * @Author Bubu
     * @date 2022/11/3 22:10
     * @commit
     */
    public static String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        int year, month, day, hour, minute, second;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DATE);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        return year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second;
    }

    /**
     * 获取当前系统日期与时间
     *
     * @return java.lang.String
     * @Author Bubu
     * @date 2022/11/3 22:11
     * @commit
     */
    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") String timeNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTime);
        return timeNow;
    }

    /**
     * 通过时间戳，转化为表示日期、时间的字符串
     *
     * @param mills 时间戳
     * @return String
     * @Author Bubu
     * @date 2022/10/26 18:42
     * @commit
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeByMills(long mills) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(mills);
    }

}
