package com.zscat.mallplus.leecode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * 环形公交路线上有 n 个站，按次序从 0 到 n - 1 进行编号。我们已知每一对相邻公交站之间的距离，distance[i] 表示编号为 i 的车站和编号为 (i + 1) % n 的车站之间的距离。

 环线上的公交车都可以按顺时针和逆时针的方向行驶。

 返回乘客从出发点 start 到目的地 destination 之间的最短距离。

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/distance-between-bus-stops
 * @Date: 2019/11/26
 * @Description
 */
public class LeeCode1 {


    public static void main(String[] args) {
        Set<String> strs = new HashSet<>();
        strs.add("1");
        strs.add("1");
        System.out.println(strs);

        System.out.println(strs.size());
    }


    public int distanceBetweenBusStops(int[] distance, int start, int destination) {
        int min = start;
        int max = destination;
        int length = distance.length;
        if(destination > length-1 || start > length -1){
            throw new RuntimeException("数组越界");
        }
        if(start > destination){
            min = destination;
            max = start;
        }
        int clockwiseDistance = 0;
        int totalDistance = 0;
        for(int i = 0; i < length;i++){
            totalDistance = totalDistance + distance[i];
        }
        for(int i = min;i<max;i++){
            clockwiseDistance = clockwiseDistance + distance[i];
        }
        int counterClockwiseDistance = totalDistance - clockwiseDistance;
        if(counterClockwiseDistance > clockwiseDistance){
            System.out.println("最短距离1为"+clockwiseDistance);
            return clockwiseDistance;
        }else{
            System.out.println("最短距离2为"+counterClockwiseDistance);
            return counterClockwiseDistance;
        }
    }
}
