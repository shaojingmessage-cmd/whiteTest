package com.java.jacoco;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.w3c.dom.NodeList;

import com.java.jacoco.structure.MyASTNode;





class InterpVisitor2 extends ASTVisitor {
	public Hashtable<String, Integer> symTable = new Hashtable<String, Integer>();
	public  Stack<Integer> stack = new Stack<Integer>(); 
	public Hashtable<Integer, ArrayList<int[]>> mapping=new Hashtable<>();
	public Map<Integer, Integer> returnMap = new HashMap<Integer, Integer>();
	public List<MyASTNode> nodeList = new ArrayList<>();
//	public ArrayList<Hashtable<Integer, Integer>> list =new ArrayList<Hashtable<Integer, Integer>>();
	public ArrayList<int[]> totalmapping = new ArrayList<int[]>();
	public Hashtable<Integer, ArrayList<int[]>> reversemapping=new Hashtable<>();
	List<Integer> prelist = new ArrayList<>();
	//if单独线上的
	public Hashtable<Integer, ArrayList<int[]>> ifmapping=new Hashtable<>();
	private MyASTNode getNode(ASTNode s) {
		MyASTNode littleNode = new MyASTNode();
		littleNode.astNode = s;
		
		return littleNode;
	}
	private void addlink(int preid,int id) {
		int[] link = { preid, id };
		totalmapping.add(link);
		
	}
	private int getPreId() {
		if(prelist.size()==0) {
			return 0;
		}
		return prelist.get(prelist.size()-1);
	}
	private void addPreId(int id) {
		prelist.add(id);
	}
	@Override
    public boolean visit(IfStatement n) { 
		int id = n.hashCode();
		int preid = getPreId();
		addPreId(id);
		if(preid>0) {
			addlink(preid, id);
		}
		NodeVisitor nv = new NodeVisitor();
		nodeList.add(getNode(n));
    //	n.getExpression().accept(this);
    	ASTNode thenstatement = n.getThenStatement();
    	ASTNode elsestatement = n.getElseStatement();
    	if(mapping.get(id)==null) {
    		mapping.put(id, new ArrayList<int[]>());
    	}
    	ASTNode bb = n.getParent().getParent();
//    	System.out.println("ifParent:\n"+n.getParent().toString());
		if(thenstatement != null){
			ASTNode aa = thenstatement.getParent().getParent();
			 
			int e = thenstatement.hashCode();
			System.out.println("thenstatementParent:\n"+thenstatement.getParent().toString());
			int[] link = { id, thenstatement.hashCode() };
			mapping.get(id).add(link);
			addlink(id, thenstatement.hashCode());
			nodeList.add(getNode(thenstatement));
			System.out.println(thenstatement.toString());
			thenstatement.accept(nv);
//			ArrayList<int[]> thenmapping = nv.totalmapping;
//			totalmapping.addAll(thenmapping);
//			nodeList.addAll(nv.getASTNodes());
        }
       
    	if(null != elsestatement) {
    		int flags = elsestatement.getFlags();
    		int[] link = { id, elsestatement.hashCode() };
    		mapping.get(id).add(link);
    		addlink(id, elsestatement.hashCode());
    		nodeList.add(getNode(thenstatement));
    		elsestatement.accept(this);
    	}else{
    		int[] link = { id, id };
    		mapping.get(id).add(link);
    	}
      
        return false;
    }
	@Override
    public boolean visit(WhileStatement n) {
		int id = n.hashCode();
    	n.getExpression().accept(this);
    	int result = stack.pop();
    	while(result == 1){
    		n.getBody().accept(this);
    		n.getExpression().accept(this);
    		result = stack.pop();
    	}
        return false;
    }

	@Override
	public void endVisit(ReturnStatement node) {
		ASTNode ss = node.getParent();
		ASTNode bb = node.getParent().getParent();
		int id = node.getParent().hashCode();
		int preid = getPreId();
		returnMap.put(id, id);
		System.out.println(id);
		if (mapping.get(preid) != null) {
			for (int[] hcode : mapping.get(preid)) {
				if (hcode.length > 0 && hcode[0] == preid&&returnMap.get(hcode[1])==null) {
					addlink(hcode[1], id);
				}
			}
		}else {
			addlink(preid, id);
		} 
		nodeList.add(getNode(node));
			
	}
	
	@Override
	public void endVisit(ExpressionStatement node) {
		ASTNode ss = node.getParent();
		ASTNode bb = node.getParent().getParent();
		int c = ss.hashCode();
		int d = bb.hashCode();
		int id = node.hashCode();
		int preid = getPreId();
		addPreId(id);
		if (mapping.get(preid) != null) {
			for (int[] hcode : mapping.get(preid)) {
				if (hcode.length > 0 && hcode[0] == preid&&returnMap.get(hcode[1])==null) {
					addlink(hcode[1], id);
				}
			}
		}else {
			addlink(preid, id);
		} 
		nodeList.add(getNode(node));
		
	}
	@Override
	public void endVisit(VariableDeclarationStatement node) {
		ASTNode ss = node.getParent();
		ASTNode bb = node.getParent().getParent();
		int id = node.hashCode();
		int preid = getPreId();
		addPreId(id);
		if (mapping.get(preid) != null) {
			for (int[] hcode : mapping.get(preid)) {
				if (hcode.length > 0 && hcode[0] == preid&&returnMap.get(hcode[1])==null) {
					addlink(hcode[1], id);
				}
			}
		}else {
			addlink(preid, id);
		}
		nodeList.add(getNode(node));
	}
}


