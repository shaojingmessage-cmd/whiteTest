package com.java.jacoco;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;


public class MethodNodeVisitor extends ASTVisitor {

	List<MethodDeclaration> methodNodeList = new ArrayList<>();

	public List<MethodDeclaration> getMethodDecs() {
		return methodNodeList;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
			methodNodeList.add(node);
			return true;
	}
	

}
