package com.java.jacoco.testclass;

public  abstract class Employee {
    private int num;
    private String name;
    private String address;
    public Employee(int num,String name,String address){
        this.num = num;
        this.name =name;
        this.address = address;
    }
    public Employee(){

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public abstract <E>void happyPrint(E[] aa);


}
