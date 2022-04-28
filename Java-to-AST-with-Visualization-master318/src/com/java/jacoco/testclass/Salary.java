package com.java.jacoco.testclass;

public class Salary extends Employee{
    private double salary;
    public Salary(int num,String name,String add){
        super(num,name,add);

    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    //泛型方法
    public <E>void happyPrint(E[] printString){
        for(E a:printString){
            System.out.println(" "+a);
        }
    }
    //有上届限制泛型的类型
    public <T extends Comparable<T>> T maxinm(T x,T y,T z){
        T max = x;
        if(y.compareTo(max)>0){
            max=y;
        }
        if(z.compareTo(max)>0){
            max=z;
        }

        return max;

    }
    //泛型类


}
