package com.java.jacoco;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.java.jacoco.structure.DotNode;
import com.java.jacoco.structure.MyASTNode;
import com.java.jacoco.structure.MyMethodNode;
import com.java.jacoco.util.FileUtil;



public class ASTGenerator {

	public List<MyMethodNode> methodNodeList = new ArrayList<MyMethodNode>();

	public ASTGenerator(File f) {
		ParseFile(f);
	}

	/**
	 * get function for methodNodeList
	 * 
	 * @return
	 */
	public List<MyMethodNode> getMethodNodeList() {
		return methodNodeList;
	}

	/**
	 * use ASTParse to parse string
	 * 
	 * @param srcStr
	 */
	public void parse(String srcStr) {
		//为了1.8降低了配置
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(srcStr.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		// 获得ast 文本转ast
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// find the MethodDeclaration node, 创建函数审查器，对函数审查可以做自己的定义
		MethodNodeVisitor methodNodeVisitor = new MethodNodeVisitor();
		// 把审查器放入ast中并进行操作
		cu.accept(methodNodeVisitor);
		// traverse all child nodes, NodeVisitor遍历获得函数的具体情况,方法声明/构造器声明

		for (MethodDeclaration m : methodNodeVisitor.getMethodDecs()) {
			// 创建函数具体的node
			MyMethodNode mNode = new MyMethodNode();
			// 记录函数来源
			mNode.methodNode = m;

			Block b = m.getBody();
			List<ASTNode> states = b.statements();
			List<Integer> aa = new ArrayList<>();
			aa.add(mNode.hashCode());
			// 这个地方会是一个递归if里面有if
			for (ASTNode node : (List<ASTNode>) b.statements()) {
				int pHashcode = aa.get(aa.size() - 1);
				int hashcode = node.hashCode();
				MyASTNode myNode = new MyASTNode();
				// 如果是判断语句特殊处理

				if (node instanceof WhileStatement || node instanceof ForStatement || node instanceof DoStatement
						|| node instanceof EnhancedForStatement || node instanceof IfStatement) {
					myNode.shape = "diamond";// 首先改变形状
					// node审查器(暂时没用)
					NodeVisitor nv = new NodeVisitor();
					node.accept(nv);
					List<ASTNode> astnodes = new ArrayList<>();
					for (ASTNode ifnode : astnodes) {
						if (node != ifnode && ifnode instanceof Statement) {
							MyASTNode littleNode = new MyASTNode();
							littleNode.astNode = ifnode;
							littleNode.shape = "record";
							int littlehashcode = ifnode.hashCode();
							System.out.println(hashcode + "-->" + littlehashcode);
							//如果return，则不计较
							if (!(ifnode instanceof ReturnStatement)) {
								littleNode.flag=littlehashcode;
							}
								int[] link = { hashcode, littlehashcode };
								mNode.mapping.add(link);
								mNode.nodeList.add(littleNode);
							
						}

					}

				}

				myNode.astNode = node;
				myNode.startLineNum = cu.getLineNumber(node.getStartPosition());
				myNode.endLineNum = cu.getLineNumber(node.getStartPosition() + node.getLength());

				mNode.nodeList.add(myNode);
				// add to mapping
				// in case, I need to exclude root node
				if (node.equals(m)) {
					continue;
				}

				// int pHashcode = node.getParent().hashCode();

				int[] link = { pHashcode, hashcode };
				mNode.mapping.add(link);
				aa.add(hashcode);
				// }

				// List<ASTNode> astnodes = nv.getASTNodes();
				// for (ASTNode node : astnodes) {
				////
				// if(!ASTNode.nodeClassForType(node.getNodeType()).getName().contains("SimpleName"))
				// {
				// MyASTNode myNode = new MyASTNode();
				// myNode.astNode = node;
				// myNode.startLineNum = cu.getLineNumber(node.getStartPosition());
				// myNode.endLineNum =
				// cu.getLineNumber(node.getStartPosition()+node.getLength());
				//// Doc   tNode myNode = new DotNode();
				//// myNode.setId( node.getParent().hashCode());
				//// myNode.setShape("record");
				//// myNode.setText(text);
				//
				//
				//
				//
				// // add to nodeList
				// mNode.nodeList.add(myNode);
				// // add to mapping
				// // in case, I need to exclude root node
				// if (node.equals(m)) {
				// continue;
				// }
				// int pHashcode = node.getParent().hashCode();
				// int hashcode = node.hashCode();
				// int[] link = { pHashcode, hashcode };
				// mNode.mapping.add(link);
				// }
				methodNodeList.add(mNode);
			}
		}
		// System.out.print(ast);
	}

	public void ParseFile(File f) {
		String filePath = f.getAbsolutePath();
		if (f.isFile()) {
			try {
				parse(FileUtil.readFileToString(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Not a File!");
		}
	}
}
