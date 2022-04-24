package com.java.jacoco;

import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

class InterpVisitor extends ASTVisitor {
	public Hashtable<String, Integer> symTable = new Hashtable<String, Integer>();
	public  Stack<Integer> stack = new Stack<Integer>(); 

	@Override
    public boolean visit(IfStatement n) { 
    	n.getExpression().accept(this);
    	Statement thenstatement = n.getThenStatement();
    	Statement elsestatement = n.getElseStatement();
    	int result = stack.pop();
        if(result == 1){
        	 thenstatement.accept(this);
        }
        else{
        	if(null != elsestatement) elsestatement.accept(this);
        }
        return false;
    }
	@Override
    public boolean visit(WhileStatement n) {
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
    public void endVisit(Assignment n) {
         
        Assignment.Operator operator = n.getOperator();
        String varName = n.getLeftHandSide().toString();
        int value = stack.pop();
        stack.pop();
        if (operator == Assignment.Operator.ASSIGN) {
            symTable.put(varName, value);
        } /*else if (operator == Assignment.Operator.PLUS_ASSIGN) {
        
        } else if (operator == Assignment.Operator.MINUS_ASSIGN) {
        
        } else if (operator == Assignment.Operator.TIMES_ASSIGN) {
        
        } else if (operator == Assignment.Operator.DIVIDE_ASSIGN) {
         
        } else if (operator == Assignment.Operator.REMAINDER_ASSIGN) {
          
        } */
        System.out.println(varName + "=" + value);
 
    }
	@Override
    public  void endVisit(InfixExpression n) {
 
        InfixExpression.Operator operator = n.getOperator();
        int leftValue,rightValue;
        rightValue = stack.pop();
        leftValue = stack.pop();        
        
        if (operator == InfixExpression.Operator.PLUS) {        	
        	stack.push(leftValue + rightValue);
        } else if (operator == InfixExpression.Operator.MINUS) {
        	stack.push(leftValue - rightValue);
        } else if (operator == InfixExpression.Operator.TIMES) {
        	stack.push(leftValue * rightValue);
        } else if (operator == InfixExpression.Operator.DIVIDE) {
        	if(rightValue == 0){
        		System.out.println("divided by zero");
        		System.exit(1);
        	}else{
        		stack.push(leftValue / rightValue);
        	}
        } else if (operator == InfixExpression.Operator.REMAINDER) {
        	if(rightValue == 0){
        		System.out.println("divided by zero");
        		System.exit(1);
        	}else{
        		stack.push(leftValue % rightValue);
        	}
        } else if (operator == InfixExpression.Operator.GREATER){
        	stack.push(leftValue > rightValue?1:0) ;
        	
        } else if (operator == InfixExpression.Operator.GREATER_EQUALS){
        	stack.push(leftValue >= rightValue?1:0) ;
        } else if (operator == InfixExpression.Operator.LESS){
        	stack.push(leftValue < rightValue?1:0) ;
        	
        } else if (operator == InfixExpression.Operator.LESS_EQUALS){
        	stack.push(leftValue <= rightValue?1:0) ;
        } else if (operator == InfixExpression.Operator.EQUALS){
        	stack.push(leftValue == rightValue?1:0) ;
        } else if (operator == InfixExpression.Operator.NOT_EQUALS){
        	stack.push(leftValue != rightValue?1:0) ;       	
        }
    }
	@Override
    public  void endVisit(SimpleName n) {
//    	String name = n.getIdentifier();
//    	if (!symTable.containsKey(name)){
//    		symTable.put(name, 0);    		
//    	}
//    	stack.push(symTable.get(name));
    }
	@Override
    public  void endVisit(NumberLiteral n){
    	stack.push(Integer.parseInt(n.getToken()));
    }
	@Override
    public  void endVisit(QualifiedName n) {
    	Name name = n.getQualifier();
    	String qualifierName = name.toString();
    	//TODO记录下调用的函数
    }
	@Override
	public void endVisit(VariableDeclarationStatement node) {
			
			
	}
	
	@Override
	public void endVisit(ExpressionStatement node) {
			
	}
}


