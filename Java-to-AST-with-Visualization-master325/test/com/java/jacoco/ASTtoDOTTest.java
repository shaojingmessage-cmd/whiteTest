package com.java.jacoco;

import com.java.jacoco.structure.MyASTNode;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ASTtoDOTTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }



    @Test
    public void test2() throws Exception {
        MyASTNode a = new MyASTNode();


        a.astNode = null;
       String str = ASTtoDOT.test(2);
        Assert.assertEquals(str,"null");
    }
}