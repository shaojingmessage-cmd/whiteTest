package com.java.jacoco.testclass;

public class Dog extends Animal{


    private String color;
    public Dog(int age, String name) {
        super(age, name);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void move(){
        System.out.println("狗可以坐");
    }
}
