package com.zscat.mallplus.admin.utils;

import java.util.LinkedList;
import java.util.Random;
import org.apache.commons.lang.StringUtils;

/**
 * @author xiang.li
 * create date 2018/12/11
 * description
 */
public class GeneratorCodeUtil {

    public static String couponCodeStr = "ABCEDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generatorString(int num) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        LinkedList<Character> characterList = new LinkedList <>();
        generatorCharSet(couponCodeStr, num, characterList, random);
        String couponCode4 = StringUtils.join(characterList, "");
        buffer.append(couponCode4);
        return buffer.toString();
    }
    private static void generatorCharSet(String code, int num, LinkedList <Character> characters, Random random) {
        for (int i = 0; i < num; i++) {
            char c = code.charAt(random.nextInt(code.length()));
            characters.offer(c);
        }
    }

    public static void main(String[] args) {
        System.out.println(generatorString(8));
    }
}
