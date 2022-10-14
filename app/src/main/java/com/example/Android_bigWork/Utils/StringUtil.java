package com.example.Android_bigWork.Utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String replaceToBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static SpannableString getSSMoney(double money,int symbolSize){
        String moneyStr= String.valueOf(money);
        SpannableString ss=new SpannableString("￥"+moneyStr);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(symbolSize);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);//加粗
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ffc107")) ,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(sizeSpan,0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(styleSpan,0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

}
