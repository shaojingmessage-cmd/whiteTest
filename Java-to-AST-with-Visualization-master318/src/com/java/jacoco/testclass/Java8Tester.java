package com.java.jacoco.testclass;

import java.util.HashMap;
import java.util.Map;

public class Java8Tester {
    public static  void main(String[] args){
        int a =5;
        int b=6;
        Java8Tester java8Tester = new Java8Tester();
//        System.out.println(java8Tester.add(a,b));
//        System.out.println(java8Tester.sub(a,b));
//        System.out.println(java8Tester.multiplication(a,b));
//        System.out.println(java8Tester.division(a,b));
        option add = (c,d)->c+d;
        option sub = (c,d)->c-d;
        System.out.println(java8Tester.printbb(a,b,add));
        System.out.println(java8Tester.printbb(a,b,sub));



    }

    interface option{
        int aaa(int a, int b);
    }
    private int  printbb(int a,int b,option p){
        Map list = new HashMap();
        return p.aaa(a,b);

    }

    //加减乘除,
    public int add(int a,int b){
        return a+b;
    }
    public int sub(int a,int b){
        return a-b;
    }
    public int multiplication(int a,int b){
        return a*b;
    }
    public int division(int a,int b){
        return a/b;
    }

}
