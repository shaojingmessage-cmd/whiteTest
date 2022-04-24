package com.java.jacoco;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import com.java.jacoco.structure.DataNode;






public class DirectChildren {
	public static int ID = 0; //用来编号
	 
	// 输入的是CompilationUnit根节点， label为0
	public static void getDirectChildren(ASTNode node, int label, Map<Integer, DataNode> Nodes){
		
		//先创建一个节点数据结构
		DataNode myDataNode = new DataNode();
		Nodes.put(label, myDataNode);
		myDataNode.label = label;
		myDataNode.astNode = node;
		myDataNode.nodeType = node.getClass().toString();
		List listProperty = node.structuralPropertiesForType();
		boolean hasChildren = false;
		for(int i = 0; i < listProperty.size(); i++){
			StructuralPropertyDescriptor propertyDescriptor = (StructuralPropertyDescriptor) listProperty.get(i);
			if(propertyDescriptor instanceof ChildListPropertyDescriptor){//ASTNode列表
				ChildListPropertyDescriptor childListPropertyDescriptor = (ChildListPropertyDescriptor)propertyDescriptor;
				Object children = node.getStructuralProperty(childListPropertyDescriptor);
				List<ASTNode> childrenNodes = (List<ASTNode>)children;
				for(ASTNode childNode: childrenNodes){
					//获取所有节点
					if(childNode == null)
						continue;
					hasChildren = true;
					myDataNode.childrenNodes.add(childNode);
					myDataNode.childrenLables.add((++ID));
					getDirectChildren(childNode, ID, Nodes);//继续递归
					System.out.println("childrenList:   "+childNode+"   "+childNode.getClass());
				}
				
			}else if(propertyDescriptor instanceof ChildPropertyDescriptor){//一个ASTNode
				ChildPropertyDescriptor childPropertyDescriptor = (ChildPropertyDescriptor)propertyDescriptor;
				Object child = node.getStructuralProperty(childPropertyDescriptor);
				ASTNode childNode = (ASTNode)child;
				if(childNode == null)
					continue;
				hasChildren = true;
				//获取了这个节点
				myDataNode.childrenNodes.add(childNode);
				myDataNode.childrenLables.add((++ID));
				getDirectChildren(childNode, ID, Nodes);//继续递归
				
				System.out.println("child:   "+childNode +"  "+childNode.getClass());
			}
		}
		if(hasChildren){
			//进行递归子节点
			myDataNode.isLeaf = false;
		}
		else{
			//结束，是叶子结点
			myDataNode.isLeaf = true;
		}
	}
	 

	//版权声明：本文为CSDN博主「同学少年」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
	//原文链接：https://blog.csdn.net/sr_19930829/article/details/83659918

}
