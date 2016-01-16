package com.ryy.util;

public class NumberUtil {

    public static String toFix(double d, int scale) {
        return String.format("%." + scale + "f", d);
    }

    public static void main(String[] args) {
    }
}
