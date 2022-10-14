package com.example.Android_bigWork.Utils;

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
}
