package com.java.jacoco.testclass;

public class Animal {
    private int age;
    private String name;
    public Animal(int age,String name){
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void move(){
        System.out.println("动物可以走动");
    }


}
