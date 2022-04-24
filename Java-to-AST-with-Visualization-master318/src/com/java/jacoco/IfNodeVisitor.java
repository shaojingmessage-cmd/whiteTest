package com.java.jacoco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.java.jacoco.structure.DotNode;



public class IfNodeVisitor extends ASTVisitor {
	//todo LabeledStatement
	

	public List<ASTNode> nodeList = new ArrayList<ASTNode>();
   public List<DotNode> dotnodeList = new ArrayList<DotNode>();
   public Map dotnodeMap = new HashMap<Integer,DotNode>();
	
	@Override
	public boolean visit(IfStatement node) {
			nodeList.add(node);
			addDiamondNodeList(node);
			return false;
	}
	@Override
	public boolean visit(WhileStatement node) {
			nodeList.add(node);
			addDiamondNodeList(node);
			return true;
	}
	@Override
	public boolean visit(ForStatement node) {
			nodeList.add(node);
			addDiamondNodeList(node);
			return true;
	}
	@Override
	public boolean visit(DoStatement node) {
			nodeList.add(node);
			addDiamondNodeList(node);
			return true;
	}
	@Override
	public boolean visit(EnhancedForStatement node) {
			nodeList.add(node);
			addDiamondNodeList(node);
			return true;
	}
	
	@Override
	public void endVisit(VariableDeclarationStatement node) {
			nodeList.add(node);
			addCircleDotNodeList(node);
			
	}
	
	@Override
	public void endVisit(ExpressionStatement node) {
			nodeList.add(node);
			addRecordDotNodeList(node);
			
	}
//	private boolean isLoopStatement(Block block) {
//	    ASTNode parent = block.getParent();
//	    return
//	            parent instanceof WhileStatement ||
//	            parent instanceof ForStatement ||
//	            parent instanceof DoStatement ||
//	            parent instanceof EnhancedForStatement ||
//	            parent instanceof IfStatement;
//	}
	private void addDiamondNodeList(ASTNode node) {
		DotNode myNode = new DotNode();
		int id = node.hashCode();
		myNode.astNode = node;
		myNode.setShape("diamond");
		myNode.setId(id);
		dotnodeList.add(myNode);
		dotnodeMap.put(id, myNode);
	}
	private void addCircleDotNodeList(ASTNode node) {
		DotNode myNode = new DotNode();
		int id = node.hashCode();
		myNode.astNode = node;
		myNode.setShape("circle");
		myNode.setId(node.hashCode());
		dotnodeList.add(myNode);
		dotnodeMap.put(id, myNode);
	}
	
	private void addRecordDotNodeList(ASTNode node) {
		DotNode myNode = new DotNode();
		int id = node.hashCode();
		myNode.astNode = node;
		myNode.setShape("record");
		myNode.setId(node.hashCode());
		dotnodeList.add(myNode);
		dotnodeMap.put(id, myNode);
	}

	public List<ASTNode> getASTNodes() {
		return nodeList;
	}
	public List<DotNode> getDotASTNodes() {
		return dotnodeList;
	}
	public Map<Integer,DotNode> getDotASTNodesMap() {
		return dotnodeMap;
	}
}
