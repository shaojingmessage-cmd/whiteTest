package com.java.jacoco.visitor;

import Main.Main;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleVisitorTest {

    @Test
    public void visit1() {
        try {
            Main.main(new String[]{"a"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void visit2() {
    }
    @Test
    public void visit3() {
    }
    @Test
    public void visit4() {
    }
    @Test
    public void visit5() {
    }
    @Test
    public void visit6() {
    }
    @Test
    public void visit7() {
    }
    @Test
    public void visit8() {
    }

}