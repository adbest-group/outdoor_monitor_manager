package com.bt.om.util;

/**
 * Created by caiting on 2018/3/28.
 */
public class GeoUtil {

    private final static double rounnd = 40076000d;

    /**
     *  用于获取给定的一个距离（单位：米），按赤道的周长计算大约的相隔度数
     **/
    public static double getDegreeFromDistance(double distance){
        double degree = 0d;

        degree = Math.round(distance*360*1000000d/rounnd)/1000000d;

        return degree;
    }

    public static void main(String[] args) {
        System.out.println(getDegreeFromDistance(500d));
    }
}
