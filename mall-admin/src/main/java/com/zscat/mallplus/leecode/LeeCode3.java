package com.zscat.mallplus.leecode;

/**
 *
 * 给定一个数字字符串 S，比如 S = "123456579"，我们可以将它分成斐波那契式的序列 [123, 456, 579]。
 形式上，斐波那契式序列是一个非负整数列表 F，且满足：
 0 <= F[i] <= 2^31 - 1，（也就是说，每个整数都符合 32 位有符号整数类型）；
 F.length >= 3；
 对于所有的0 <= i < F.length - 2，都有 F[i] + F[i+1] = F[i+2] 成立。
 另外，请注意，将字符串拆分成小块时，每个块的数字一定不要以零开头，除非这个块是数字 0 本身。
 返回从 S 拆分出来的所有斐波那契式的序列块，如果不能拆分则返回 []。
 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/split-array-into-fibonacci-sequence
 * @Date: 2019/11/26
 * @Description
 */
public class LeeCode3 {

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        System.out.println("开始时间："+startTime);
        System.out.println(fibon(50));
        Long endTime = System.currentTimeMillis();
        System.out.println("结束时间："+endTime);
        System.out.println("用时："+(endTime-startTime));

    }

    public static int fibon(int n){
        if(n <= 2){
            return 1;
        }
        return fibon(n-1)+fibon(n-2);
    }

    public static int fibonac(int a,int b,int n){
        if(n > 2){
            return fibonac(a+b,a ,n-1);
        }
        return a;
    }
}
