package com.zscat.mallplus.leecode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 给你一个日期，请你设计一个算法来判断它是对应一周中的哪一天。

 输入为三个整数：day、month 和 year，分别表示日、月、年。

 您返回的结果必须是这几个值中的一个 {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}。

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/day-of-the-week
 * @Date: 2019/11/26
 * @Description
 */
public class LeeCode2 {

    public static String dayOfTheWeek(int day, int month, int year) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer sb = new StringBuffer();
        sb.append(year);
        if(String.valueOf(month).length() == 1){
            sb.append("-").append("0").append(month);
        }else{
            sb.append("-").append(month);
        }
        if(String.valueOf(day).length() == 1){
            sb.append("-").append("0").append(day);
        }else{
            sb.append("-").append(day);
        }
        Map<String,String> weekMap = new HashMap<>();
        weekMap.put("1","Sunday");
        weekMap.put("2","Monday");
        weekMap.put("3","Tuesday");
        weekMap.put("4","Wednesday");
        weekMap.put("5","Thursday");
        weekMap.put("6","Friday");
        weekMap.put("7","Saturday");
        String weekStr = null;
        try {
            Date date = sdf.parse(sb.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            weekStr = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weekMap.get(weekStr);
    }

    public static void main(String[] args) {
        System.out.println(dayOfTheWeek(3,12,2019));;
    }
}
