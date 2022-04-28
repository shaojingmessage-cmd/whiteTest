package com.java.jacoco.testclass;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String [] args){
        Animal a = new Animal(1,"huanhuan");
        List<Animal> animalList = new ArrayList<>();
        animalList.add(new Dog(2,"huahua"));
        Salary s = new Salary(101,"naem","aaaddd");


        s.happyPrint(new Integer[]{1, 2, 3, 4, 5});


    }
}
