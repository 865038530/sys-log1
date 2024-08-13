package com.example.wangjian.syslog1.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Works {

    public static void main(String[] args) {
//        int[] arr = new int[]{3,6,7,66,88,9,34,2,33,91,26,73};
//        List<Integer> list = new ArrayList<>();
//        for (int i=0;i<arr.length;i++) {
//            list.add(arr[i]);
//        }
//        List temp = list.stream().sorted().collect(Collectors.toList());
//        System.out.println(temp);
//
//        int[] arr1 = new int[]{3,6,7,66,88,9,34,2,33,91,26,73};
//        for(int j=0;j<arr1.length;j++) {
//            for(int k=0;k<arr1.length;k++) {
//                if (arr1[j] > arr1[k]) {
//                    int temps = arr1[k];
//                    arr1[k] = arr1[j];
//                    arr1[j] = temps;
//                }
//            }
//
//        }
//        for(int j=0;j<arr1.length;j++) {
//            System.out.println(arr1[j]);
//        }

        int res = 1;
        int sum = 0;
        for (int i=9;i > 0;i--) {
            int a = (res + 1) *2;
            System.out.println("第"+i+"天个数="+a);
            res = a;
            sum += res;
        }

        System.out.println("一共个数"+sum);

    }

}
