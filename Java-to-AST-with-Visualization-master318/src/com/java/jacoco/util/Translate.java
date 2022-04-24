package com.java.jacoco.util;

import java.util.HashMap;
import java.util.Map;

public class Translate {
	public static Map<String, String> translateData = new HashMap<>();
	static {
		translateData.put("VariableDeclarationStatement", "变量声明语句");
		translateData.put("IfStatement", "条件语句");
		translateData.put("ExpressionStatement", "定义变量");
		translateData.put("SimpleName", "简单名");
		translateData.put("VariableDeclarationExpression", "变量声明表达式");
		translateData.put("ForStatement", "循环语句");
		translateData.put("InfixExpression", "插入表达式");
		translateData.put("PrefixExpression", "前缀表达式");
		translateData.put("MethodInvocation", "函数调用");
		translateData.put("Assignment", "变量赋值");
		translateData.put("NullLiteral","空赋值");
		translateData.put("StringLiteral","字符串赋值");
		translateData.put("ReturnStatement","返回");
		translateData.put("ClassInstanceCreation","创建对象");
		translateData.put("ParenthesizedExpression","圆括号表达式");
		translateData.put("QualifiedName","QualifiedName");
		
	}

	public Translate() {

	}

}
