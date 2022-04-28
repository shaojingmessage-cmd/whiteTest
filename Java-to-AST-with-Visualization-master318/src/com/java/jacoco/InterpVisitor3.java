package com.java.jacoco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
;

class InterpVisitor3 extends ASTVisitor {

	public Hashtable<Integer, ArrayList<Integer>> reversemapping = new Hashtable<>();


	
	@Override
	public void preVisit(ASTNode node) {
		if(node.getParent() instanceof Block) {
			int blockId = node.getParent().hashCode();
			Block b = (Block) node.getParent();
			List<ASTNode> list = b.statements();
			if(reversemapping.get(blockId)==null) {
				List<Integer> statelist = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					ASTNode pic = list.get(i);
					statelist.add(pic.hashCode());
				}
				reversemapping.put(blockId, (ArrayList<Integer>) statelist);
			}
		}
	}
	
	
}
