package com.gloiot.hygo.utlis;


import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    // 如果字符串为空，则返回 --
    public static String isEmpyt(String str){
        if(str==null || str.equals("")){
            return "无";
        }else{
            return str;
        }
    }


    // 判断一个字符串是否全部都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }


    // 判断一个字符串是否含有数字
    public static boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    // 判断一个字符串中是否含有大写字母
    public static boolean isAcronym(String word)
    {
        for(int i = 0; i < word.length(); i++)
        {
            char c = word.charAt(i);
            if (Character.isUpperCase(c))
            {
                return true;
            }
        }
        return false;
    }

    // 判断一个字符串中是否包含<<<<<字母>>>>>>>
    public static boolean judgeContainsStr(String cardNum) {
        String regex=".*[a-zA-Z]+.*";
        Matcher m=Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    // 判断一个字符串中是否有包含<<<<<<小写字母>>>>>>>>
    public static boolean judgeContainsLowercaseStr(String cardNum) {
        String regex=".*[a-z]+.*";
        Matcher m=Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    // 判断一个字符串中是否包含<<<<<<大写字母>>>>>
    public static boolean judgeContainsCapitalStr(String cardNum) {
        String regex=".*[A-Z]+.*";
        Matcher m=Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }


    //判断一个字符串是否由：字母+数字组成,并且必须包含一个大写字母
    public static boolean judgePasswordFormat(String content){
        Log.e("查看返回的字符验证：",content+"--HasDigit:"+HasDigit(content)+"--是否包含小写:"+judgeContainsLowercaseStr(content)+"是否包含大写："+judgeContainsCapitalStr(content)+"--isAcronym:"+isAcronym(content));
        if(HasDigit(content) &&judgeContainsCapitalStr(content)&&judgeContainsLowercaseStr(content)){
            return true;
        }else{
            return false;
        }
    }

}
