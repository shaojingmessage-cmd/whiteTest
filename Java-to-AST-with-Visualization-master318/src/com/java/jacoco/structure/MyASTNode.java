package com.java.jacoco.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class MyASTNode {

	public ASTNode astNode;
	public int id;
	public int startLineNum;
	public int endLineNum;
	public String shape = "record";
	public int flag;
	private List preIds=new ArrayList<Integer>();// 接到节点的id

	private Map edgeLabels;// 指向本节点的边的标签
	public String label="";
	
	
	public List<Integer> getPreIds() {
		return preIds;
	}

	public void setPreIds(List<Integer> preIds) {
		this.preIds = preIds;
	}

	public Map getEdgeLbls() {
		return this.edgeLabels;
	}

	public String getEdgeLbl(int key) {
		return edgeLabels.get(key) == null ? "" : (String) edgeLabels.get(key);
	}

	public void setEdgeLbl(Integer preId, String label) {
		edgeLabels.put(preId, label);
	}

}
