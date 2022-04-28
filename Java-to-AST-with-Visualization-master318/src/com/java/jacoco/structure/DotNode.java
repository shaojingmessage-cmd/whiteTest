package com.java.jacoco.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class DotNode {
	public ASTNode astNode;

	private int id;// 节点的id，dot文件中作为节点的唯一标识

	private String text;// 将显示在节点框中的内容

	private String shape;// 节点的形状，只用了4种，单圆开始，双圆结束，普通四方，判断菱形

	private List preIds=new ArrayList<Integer>();// 接到节点的id

	private Map edgeLabels;// 指向本节点的边的标签

	public DotNode() {

		this.text = "";

		this.shape = "record";

		this.preIds = new ArrayList<Integer>();
		this.edgeLabels = new HashMap<>();

	}

	public int getId() {

		return id;

	}

	public void setId(int id) {

		this.id = id;

	}

	public String getText() {

		return text;

	}

	public void setText(String text) {

		this.text = text;

	}

	public String getShape() {

		return shape;

	}

	public void setShape(String shape) {

		this.shape = shape;

	}

	public List getPreIds() {

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

	/**
	 * 
	 * 在switch语句的case条件中，简单添加switch标志语句作为前导节点
	 * 
	 * @param preId
	 * 
	 */

	public void addPreId(int preId) {

		boolean exist = false;

		for (int pid :  (ArrayList<Integer>)preIds) {

			if (preId == pid) {

				exist = true;

				break;

			}

		}

		if (!exist) {

			preIds.add(preId);

		}

	}

	/**
	 * 
	 * 添加前导节点，如前导节点为判断节点，则设置边的标记为no
	 * 
	 * @param list
	 * 
	 * @param preId
	 * 
	 */

	public void addPreId(ArrayList<DotNode> list, int preId) {

		boolean exist = false;

		for (int pid : (ArrayList<Integer>)preIds) {

			if (preId == pid) {

				exist = true;

				break;

			}

		}

		if (!exist) {

			/*
			 * if(preIds.size() == 0){
			 * 
			 * level++;
			 * 
			 * for(DotNode node : list){
			 * 
			 * if(node.getPreIds().contains(id)){
			 * 
			 * node.setLevel(node.getLevel() + 1);
			 * 
			 * }
			 * 
			 * }
			 * 
			 * }
			 */

			preIds.add(preId);

			DotNode pre = getDotNode(list, preId);

			if (pre != null) {

				if (pre.getShape().equals("diamond") && getEdgeLbl(preId).equals("")) {

					setEdgeLbl(preId, "no");

				}

			}

		}

	}

	/**
	 * 
	 * 在结点链表中查找指定id的节点
	 * 
	 * @param list
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 */

	public static DotNode getDotNode(ArrayList<DotNode> list, int id) {
		
		for (DotNode d : list) {

			if (d.getId() == id) {

				return d;

			}

		}

		return null;

	}

	/**
	 * 
	 * 将toAdd合并到dest中，过滤掉重复的节点
	 * 
	 * @param dest
	 * 
	 * @param toAdd
	 * 
	 */

	public static void listAdd(ArrayList<DotNode> dest, ArrayList<DotNode> toAdd) {

		if (toAdd == null) {

			return;

		}

		if (dest == null) {

			dest = new ArrayList<>();

		}

		for (DotNode addNode : toAdd) {

			boolean exist = false;

			for (DotNode node : dest) {

				if (addNode.getId() == node.getId()) {

					exist = true;

					break;

				}

			}

			if (!exist) {

				dest.add(addNode);

			}

		}

	}

	public static void listAdd(ArrayList<DotNode> destList, DotNode node) {

		if (node == null) {

			return;

		}

		if (destList == null) {

			destList = new ArrayList<>();

		}

		boolean exist = false;

		for (DotNode d1 : destList) {

			if (node.getId() == d1.getId()) {

				exist = true;

				break;

			}

		}

		if (!exist) {

			destList.add(node);

		}

	}

}

