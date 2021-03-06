/*
 * This file was automatically generated by EvoSuite
 * Wed Apr 20 03:12:17 GMT 2022
 */

package com.java.jacoco;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.java.jacoco.ASTGenerator;
import com.java.jacoco.structure.MyMethodNode;
import java.io.File;
import java.util.List;
import org.eclipse.jdt.internal.core.index.Index;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class ASTGenerator_ESTest extends ASTGenerator_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn((String) null).when(file0).getAbsolutePath();
      doReturn(false).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      aSTGenerator0.methodNodeList = null;
      aSTGenerator0.getMethodNodeList();
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn((String) null).when(file0).getAbsolutePath();
      doReturn(false).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      aSTGenerator0.parse("^v+?H)=]Sdc");
      // Undeclared exception!
      aSTGenerator0.parse("^v+?H)=]Sdc");
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn((String) null).when(file0).getAbsolutePath();
      doReturn(false).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      // Undeclared exception!
      try { 
        aSTGenerator0.parse((String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("com.java.jacoco.ASTGenerator", e);
      }
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn("record").when(file0).getAbsolutePath();
      doReturn(true).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      MockFile mockFile0 = new MockFile(" 9dET");
      mockFile0.createNewFile();
      aSTGenerator0.parse("/=");
      // Undeclared exception!
      aSTGenerator0.ParseFile(mockFile0);
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn((String) null).when(file0).getAbsolutePath();
      doReturn(false).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      // Undeclared exception!
      try { 
        aSTGenerator0.ParseFile((File) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("com.java.jacoco.ASTGenerator", e);
      }
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      ASTGenerator aSTGenerator0 = null;
      try {
        aSTGenerator0 = new ASTGenerator((File) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("com.java.jacoco.ASTGenerator", e);
      }
  }

  @Test(timeout = 4000)
  public void test6()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn("record").when(file0).getAbsolutePath();
      doReturn(true).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      File file1 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn("").when(file1).getAbsolutePath();
      doReturn(false).when(file1).isFile();
      aSTGenerator0.ParseFile(file1);
  }

  @Test(timeout = 4000)
  public void test7()  throws Throwable  {
      Index index0 = new Index("p%Bo. 9", "p%Bo. 9", true);
      File file0 = index0.getIndexFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      ASTGenerator aSTGenerator1 = new ASTGenerator(file0);
  }

  @Test(timeout = 4000)
  public void test8()  throws Throwable  {
      File file0 = mock(File.class, new ViolatedAssumptionAnswer());
      doReturn("==").when(file0).getAbsolutePath();
      doReturn(false).when(file0).isFile();
      ASTGenerator aSTGenerator0 = new ASTGenerator(file0);
      List<MyMethodNode> list0 = aSTGenerator0.getMethodNodeList();
      assertTrue(list0.isEmpty());
  }
}
