package com.zscat.mallplus.admin.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Date: 2020/3/16
 * @Description
 */
public class Test {
    public static void main(String[] args) {
        List<DateTest> dateTests = new ArrayList<>();
        DateTest dateTest1 = new DateTest();
        dateTest1.setBegDate("2020-12-01");
        DateTest dateTest2 = new DateTest();
        dateTest2.setBegDate("2020-12-05");
        DateTest dateTest3 = new DateTest();
        dateTest3.setBegDate("2020-12-03");
        dateTests.add(dateTest1);
        dateTests.add(dateTest2);
        dateTests.add(dateTest3);
        System.out.println("排序之前的数据："+dateTests);
        dateTests.sort(new Comparator<DateTest>() {
            @Override
            public int compare(DateTest o1, DateTest o2) {
                return o1.getBegDate().compareTo(o2.getBegDate());
            }
        });
        System.out.println("排序之后的数据："+dateTests);
    }
}

class DateTest{
    private String begDate;

    public String getBegDate() {
        return begDate;
    }

    public void setBegDate(String begDate) {
        this.begDate = begDate;
    }
}
