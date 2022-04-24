package com.java.jacoco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.java.jacoco.structure.MyASTNode;



public class NodeVisitor extends ASTVisitor {

	public List<MyASTNode> nodeList = new ArrayList<MyASTNode>();
	public HashMap<Integer, HashMap<String,List<Integer>>> ifbranch 
	= new HashMap<>();
	public ArrayList<int[]> totalmapping = new ArrayList<int[]>();
	public int preid = 0;

	private int getPreId() {
		List<Integer> branch =ifbranch.get(preid).get("if");
		if(branch.size()==0) {
			return preid;
		}
		return branch.get(branch.size()-1);
	}
	private void addlink(int preid,int id) {
		int[] link = { preid, id };
		totalmapping.add(link);
		
	}
	private MyASTNode getNode(ASTNode s) {
		MyASTNode littleNode = new MyASTNode();
		littleNode.astNode = s;
		
		return littleNode;
	}
	private void voidmethod(ASTNode node) {
		ASTNode aa = node.getParent();
		System.out.println(aa.toString());
		preid = node.getParent().hashCode();
		if(ifbranch.get(preid)==null) {
			HashMap<String,List<Integer>> value= new HashMap<>();
			value.put("if", new ArrayList<Integer>());
			value.put("else", new ArrayList<Integer>());
			ifbranch.put(preid, value);
		}
	}
	@Override
	public void preVisit(ASTNode node) {
	
	}

	public List<MyASTNode> getASTNodes() {
		return nodeList;
	}
	@Override
	public void endVisit(ExpressionStatement node) {
		voidmethod(node);
		int id = node.hashCode();
		int arraypreid = getPreId();
		ifbranch.get(preid).get("if").add(id);
		addlink(arraypreid, id);
		nodeList.add(getNode(node));
	}
	@Override
	public void endVisit(VariableDeclarationStatement node) {
		voidmethod(node);
		int id = node.hashCode();
		int arraypreid = getPreId();
		ifbranch.get(preid).get("if").add(id);
		addlink(arraypreid, id);
		nodeList.add(getNode(node));
	}
	@Override
	public void endVisit(IfStatement node) {
		
	}
}
