package com.java.jacoco.testclass;

import java.util.ArrayList;
import java.util.List;

public class Noob<T>{
    private T t;
    public Noob(){

    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
    public String sub(T t1,T t2){
        String s=t1.toString()+t2.toString();
        return s;

    }
}

