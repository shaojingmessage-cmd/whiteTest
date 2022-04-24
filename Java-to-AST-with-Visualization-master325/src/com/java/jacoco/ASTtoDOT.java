package com.java.jacoco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.java.jacoco.structure.MyASTNode;
import com.java.jacoco.structure.MyMethodNode;
import com.java.jacoco.util.ExcelUtils;
import com.java.jacoco.util.Graph;
import com.java.jacoco.util.GraphOperations;
import com.java.jacoco.util.Translate;
import com.java.jacoco.util.Vertex;



public class ASTtoDOT {

	/**
	 * Convert a method node to .dot string
	 * 
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static String ASTtoDotParser(MyMethodNode m) throws Exception {
		// 初始化
		Graph g = new Graph();
		GraphOperations go = new GraphOperations(g);
		List<String> vertex = new ArrayList<>();
		Map<String, String> vertexCode = new HashMap<>();
		Map<Integer, String> labelMap = new HashMap<>();
		String lastone = "1111";
		String javadoc = "警告！！没有注释";
		if (m.methodNode.getJavadoc() != null) {
			javadoc = m.methodNode.getJavadoc().toString();
		}
		String str = "digraph \"DirectedGraph\" {\n";
		// name
		str += ("graph [label = \"" + m.methodNode.getName() + "\", labelloc=t, concentrate = true ];\n");
		str += ("node [fontname = \"FangSong\" ];\n");
		str += ("\"" + String.valueOf(m.methodNode.getBody().hashCode()) + "\" [ label=\"参数："
				+ m.methodNode.parameters() + "\n " + javadoc + "\" ];\n");
		for (MyASTNode mn : m.nodeList) {
			ASTNode astNode = mn.astNode;
			int hashcode = 0;

			if (astNode != null) {

				hashcode = astNode.hashCode();
				int nodeType = astNode.getNodeType();
				// " startLineNumber="
				// + String.valueOf(mn.startLineNum)+" endLineNumber="
				// + String.valueOf(mn.endLineNum) +
				str += ("\"" + String.valueOf(hashcode) + "\" [ label=\"" + buildLabel(mn) + "\" type="
						+ String.valueOf(nodeType) + " shape=" + String.valueOf(mn.shape) + " ]\n");
				if (!mn.label.isEmpty()) {
					labelMap.put(hashcode, mn.label);
				}
				vertexCode.put(String.valueOf(hashcode), buildLabelFormate(mn));
			} else {
				hashcode = 1111;
				str += ("\"" + String.valueOf(hashcode) + "\" [ label=\"" + "end" + "\" " + " shape=box" + " ]\n");
				vertexCode.put(String.valueOf(hashcode), "end");
			}
			vertex.add(String.valueOf(hashcode));

		}
		for (String code : vertex) {
			g.addVertex(code, vertexCode.get(code));
		}

		for (int[] k : m.mapping) {
			int pHashcode = k[0];
			int hashcode = k[1];
			if (labelMap.get(hashcode) != null) {
				str += ("\"" + String.valueOf(pHashcode) + "\" -> \"" + String.valueOf(hashcode) + "\"" + " [ label=\""
						+ labelMap.get(hashcode) + "\"]" + "\n");
			} else {
				str += ("\"" + String.valueOf(pHashcode) + "\" -> \"" + String.valueOf(hashcode) + "\"\n");
			}
			// if (vertex.contains(String.valueOf(pHashcode)) &&
			// vertex.contains(String.valueOf(hashcode))) {
			g.addEdge(String.valueOf(pHashcode), String.valueOf(hashcode));//
			// }
		}

		str += "}\n";
		if (vertex.size() > 0) {
			String first = vertex.get(0);
			List<LinkedList<Vertex>> paths = go.getResult(first, lastone);
			ExcelUtils.testTxt(m.methodNode.getName().toString(), paths, labelMap, "参数：" + m.methodNode.parameters());
			ExcelUtils.test(m.methodNode.getName().toString(), paths, labelMap, "参数：" + m.methodNode.parameters());
		}
		// 遍历获得路径

		return str;
	}

	/**
	 * Configure the label, i.e., what you want to display in the visulization
	 * 
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public static String buildLabel(MyASTNode node) throws Exception {
		// .replace("\n", " ")
		String contentString = node.astNode.toString().replace("\n", " ").replace("\"", "\\\"").replace("  ", " ")
				.replace("{", " ").replace("}", " ").replace(">", "\\> ").replace("<", "\\< ");
		if (node.astNode instanceof IfStatement) {
			node.shape = "diamond";
			IfStatement state = (IfStatement) node.astNode;
			contentString = state.getExpression().toString().replace("\n", " ").replace("\"", "\\\"").replace("  ", " ")
					.replace("{", " ").replace("}", " ").replace(">", "\\> ").replace("<", "\\< ");
		}

		String nodeType = ASTNode.nodeClassForType(node.astNode.getNodeType()).getName()
				.replace("org.eclipse.jdt.core.dom.", "");
		// + "," + node.startLineNum + "," + node.endLineNum
		nodeType = getUsedType(node.astNode);
		// + "," + node.startLineNum + "," + node.endLineNum
		System.out.println(nodeType);
		return "([" + node.astNode.hashCode() + "]" + contentString + "," + nodeType + ")";

	}

	/**
	 * Configure the label, i.e., what you want to display in the visulization
	 * 
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public static String buildLabelFormate(MyASTNode node) throws Exception {
		// .replace("\n", " ")
		String contentString = node.astNode.toString();
		if (node.astNode instanceof IfStatement) {
			node.shape = "diamond";
			IfStatement state = (IfStatement) node.astNode;
			contentString = state.getExpression().toString();
		}

		String nodeType = ASTNode.nodeClassForType(node.astNode.getNodeType()).getName()
				.replace("org.eclipse.jdt.core.dom.", "");
		nodeType = getUsedType(node.astNode);
		// + "," + node.startLineNum + "," + node.endLineNum
		System.out.println(nodeType);
		return contentString.replace("\n", " ") + "//" + nodeType;

	}

	public static String getUsedType(ASTNode node) {

		String nodeType = "";
		Expression express = null;
		if (node instanceof VariableDeclarationStatement) {
			List fragments = ((VariableDeclarationStatement) node).fragments();
			for (Object obj : fragments) {
				VariableDeclarationFragment v = (VariableDeclarationFragment) obj;
				express = v.getInitializer();
				String str1 = Translate.translateData.get(ASTNode.nodeClassForType(express.getNodeType())
						.getName().replace("org.eclipse.jdt.core.dom.", ""));
				String str2 = ASTNode.nodeClassForType(express.getNodeType())
						.getName().replace("org.eclipse.jdt.core.dom.", "");
				nodeType = "初始化赋值|" +(str1 ==null?str2:str1);
			
			}
		} else if (node instanceof ExpressionStatement) {
			express = ((ExpressionStatement) node).getExpression();
			boolean flag = false;
			if (express instanceof Assignment) {
				Expression exp = ((Assignment) express).getRightHandSide();
				if (exp != null) {
					express = exp;
					flag = true;
				}

			}
			String str1 = Translate.translateData.get(ASTNode.nodeClassForType(express.getNodeType())
					.getName().replace("org.eclipse.jdt.core.dom.", ""));
			String str2 = ASTNode.nodeClassForType(express.getNodeType())
					.getName().replace("org.eclipse.jdt.core.dom.", "");
			if (!flag) {
				nodeType =(str1 ==null?str2:str1);
			} else {
				nodeType = "变量赋值|" + (str1 ==null?str2:str1);
			}

		} else {
			String str1 = Translate.translateData.get(ASTNode.nodeClassForType(node.getNodeType())
					.getName().replace("org.eclipse.jdt.core.dom.", ""));
			String str2 = ASTNode.nodeClassForType(node.getNodeType())
					.getName().replace("org.eclipse.jdt.core.dom.", "");
			nodeType = (str1 ==null?str2:str1);
			
		}
		
	

		return nodeType;

	}
	public static String test(int a){
	System.out.println("hello");
	if(a==1){
		return "hello";
	}else{
		return "null";
	}



	}

	
}
