package com.java.jacoco.structure;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.java.jacoco.structure.MyASTNode;

public class MyMethodDotNode {

	public MethodDeclaration methodNode = null;
	public List<DotNode> nodeList = null;
	public List<int[]> mapping = null;
	

//	public List<int[]> mapping = null;

	public MyMethodDotNode() {
		this.methodNode = null;
		this.nodeList = new ArrayList<DotNode>();
		this.mapping = new ArrayList<int[]>();
	}

}
