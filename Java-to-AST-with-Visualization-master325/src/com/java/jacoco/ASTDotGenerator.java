package com.java.jacoco;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.java.jacoco.structure.DataNode;
import com.java.jacoco.structure.DotNode;
import com.java.jacoco.structure.MyASTNode;
import com.java.jacoco.structure.MyMethodDotNode;
import com.java.jacoco.structure.MyMethodNode;
import com.java.jacoco.util.FileUtil;
import com.java.jacoco.visitor.SimpleVisitor;





public class ASTDotGenerator {

	public List<MyMethodNode> methodNodeList = new ArrayList<MyMethodNode>();

	public ASTDotGenerator(File f) {
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
		
		AST ast = AST.newAST(AST.JLS17,true);
		Expression endNode =ast.newNumberLiteral("1111;");
		ASTNode endStatement = ast.newExpressionStatement(endNode);
		

		ASTParser parser = ASTParser.newParser(AST.JLS17);
		
		parser.setSource(srcStr.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		// ??????ast ?????????ast
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		// find the MethodDeclaration node, ???????????????????????????????????????????????????????????????
		MethodNodeVisitor methodNodeVisitor = new MethodNodeVisitor();
		// ??????????????????ast??????????????????
		cu.accept(methodNodeVisitor);
		// traverse all child nodes, NodeVisitor?????????????????????????????????,????????????/???????????????
//		Map<Integer, List<Integer>> rold = new HashMap<Integer, List<Integer>>();

		for (MethodDeclaration m : methodNodeVisitor.getMethodDecs()) {
//			List<Integer> list = new ArrayList<>();
//			list.add(m.hashCode());
			// ?????????????????????node
			MyMethodNode mNode = new MyMethodNode();
			// ??????????????????
			mNode.methodNode = m;

			System.out.println(m.getName());
			System.out.println(m.parameters());
//			IfVisitor v = new IfVisitor();
//			GeneralVisitor v = new GeneralVisitor();
			SimpleVisitor v = new SimpleVisitor();
			if(m.getBody()==null) {
				System.out.println(m.getName()+"-----------"+"no body");
				continue;
			}
			int rootId=m.getBody().hashCode();
			v.rootId = rootId;
			ASTNode tempbody = m.getBody();
			
			List<ASTNode> tempStatement=m.getBody().statements();
		//	tempStatement.add(endStatement);
			int passNum=0;
			for(ASTNode a:tempStatement) {
				if(!(a instanceof ForStatement||a instanceof EnhancedForStatement || a instanceof IfStatement)) {
					passNum++;
				}
			}
//			if(passNum<=4) {
//				System.out.println(m.getName()+"----------->>"+"skip");
//				continue;
//			}
			v.reversemapping.put(rootId, tempStatement);
			v.stack.push(0);
			m.accept(v);
			
			mNode.nodeList.addAll(v.canUseNodeList);
			mNode.mapping.addAll(v.canUseLink);
			

			methodNodeList.add(mNode);
			
		}

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
