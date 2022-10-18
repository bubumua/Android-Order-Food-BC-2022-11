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
import android.text.style.StyleSpan;
import android.util.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String replaceToBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

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

    public static String getCurrentDateAndTime(){
        Calendar c = Calendar.getInstance();
        int year,month,day,hour,minute,second;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DATE);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        second=c.get(Calendar.SECOND);
        return year+"/"+month+"/"+day+"-"+hour+":"+minute+":"+second;
    }

    public static String getCurrentTime(){
        long currentTime = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") String timeNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTime);
        return timeNow;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeByMills(long mills){
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(mills);
    }

}
