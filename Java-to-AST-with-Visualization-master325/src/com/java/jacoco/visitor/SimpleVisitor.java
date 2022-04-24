package com.java.jacoco.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import com.java.jacoco.structure.MyASTNode;
/**
 * 第二版for循环里的条件有些路线是不能通过的，要更加精细处理，
 * 此处不做修改了，可更迭为第三版处理
 * @author shaojing
 *
 */
public class SimpleVisitor extends ASTVisitor {
	
	public int rootId = 0;
	// 记录进入这个visitor创建的能用的node
	public List<MyASTNode> canUseNodeList = new ArrayList<>();
	public Map<Integer, MyASTNode> canUseNodeMap = new HashMap<>();
	// 记录进入这个visitor创建的能用的link
	public ArrayList<int[]> canUseLink = new ArrayList<int[]>();
	// 大括号内的所有语句，以大括号的code为key
	public Hashtable<Integer, List<ASTNode>> reversemapping = new Hashtable<>();
	// if所有分支结果的语句，以if的code为key 或者 for
	public Hashtable<Integer, List<Integer>> lastpoint = new Hashtable<>();
	// 当前if的code的记录分支在哪个if上
	public Stack<Integer> stack = new Stack<Integer>();
	// 当前的前置节点，每次进来清空
	public List<Integer> preids = new ArrayList<>();
	/** ----------------------------------for--------------------------------- **/
	// 大括号内的所有语句，以大括号的code为key
	public Hashtable<Integer, List<ASTNode>> formapping = new Hashtable<>();
	public List<Integer> rootStateList = new ArrayList<>();

	/**
	 * ----------------------------------enhancedFor---------------------------------
	 **/

	public HashMap<Integer, Integer> exchangeId = new HashMap<>();
	public Hashtable<Integer, List<Integer>> enhancedforResult = new Hashtable<>();

	public List<Integer> returnList = new ArrayList<>();

	private void addlink(MyASTNode n) {
		List<Integer> preidList = n.getPreIds();
		int id = 1111;
		if (n.astNode != null) {
			id = n.astNode.hashCode();
		}
		canUseNodeList.add(n);
		canUseNodeMap.put(id, n);
		for (int preid : preidList) {
			int tempid = preid;
			// 对于图形做的特殊处理
			if (exchangeId.containsKey(preid)) {
				tempid = exchangeId.get(preid);
			}
			int[] link = { tempid, id };
			canUseLink.add(link);
		}

	}

	/**
	 * 获得包装节点
	 * 
	 * @param n
	 * @return
	 */
	private MyASTNode getNode(ASTNode n) {
		MyASTNode m = new MyASTNode();
		m.astNode = n;
		// int id = n.hashCode();
		// if (lastpoint.get(id) == null) {
		// //创建if的分支记录
		// List<Integer> ifmap = new ArrayList<>();
		// lastpoint.put(id, ifmap);
		// }
		return m;
	}

	/**
	 * 获得前置节点的所有结果集
	 * 
	 * @param id
	 *            ifcode
	 * 
	 * @param list
	 * @return
	 */
	private List<Integer> getResultList(int id, List<Integer> list) {
		if (lastpoint.get(id) != null && lastpoint.get(id).size() > 0) {
			// 递归获得前置语句的所有结果list

			/** 原方法list = getlastPoint(id, list); **/
			list = getNormalResultPoint(id, list);

		} else {
			// 大括号的前置节点没有后置节点，那大括号内的前置节点就是当前节点的前置节点
			list.add(id);
		}
		return list;
	}

	// 大括号内遍历获得本节点的前置节点
	/**
	 * 
	 * @param ppids
	 *            大括号下所有语句的序列
	 * @param id
	 *            当前节点id
	 * @param parentid
	 *            当前节点的大括号id
	 * @return 获得大括号内某code的前置节点
	 */
	public List<Integer> getPreids(List<ASTNode> ppids, int id, int parentid, List<Integer> list) {
		// 寻找当前节点id属于哪个位置
		for (int i = 0; i < ppids.size(); i++) {
			if (ppids.get(i).hashCode() != id) {
				continue;
			}
			if (i == 0) {// 如果是第一个语句
				List<Integer> firstPids = new ArrayList<>();
				// 是if的括号，替换成ifcode 不是if的括号，用括号的code
				int listParentId = stack.pop();
				if (listParentId == 0) {// 不是if的括号，用括号的code
					firstPids.add(parentid);
					return firstPids;

				} else {
					// 这个全是ifcode-》listParentId
					firstPids.add(listParentId);
					return firstPids;

				}
			} else {
				// 获得主路上的前一个点id
				int ppid = ppids.get(i - 1).hashCode();
				// 针对id寻找他的结果集
				return getResultList(ppid, list);
			}
		}
		return getResultList(parentid, list);
	}

	/**
	 * 处理分支情况，传入大括号内所有 parentid是大括号code
	 * 
	 * @param b
	 */
	public void branch(Statement b,String label) {

		List<ASTNode> list = new ArrayList<>();
		if (b instanceof Block) {
			list = ((Block) b).statements();
		} else {
			list.add(b);
		}
		// 括号的code
		int parentid = b.hashCode();
		int lastIfCode = b.getParent().hashCode();
		// 括号的list顺序
		if (reversemapping.get(parentid) == null) {
			reversemapping.put(parentid, list);
		}
		StringBuffer expressStr = new StringBuffer();
		// 遍历大括号语句
		for (int i = 0; i < list.size(); i++) {
			ASTNode pic = list.get(i);
			// 创建node
			MyASTNode mypic = getNode(pic);

			if (pic instanceof IfStatement || pic instanceof ForStatement || pic instanceof EnhancedForStatement
					|| pic instanceof TryStatement) {
				pic.accept(this);// 迭代
				if (i == list.size() - 1) {

					List<Integer> lastIfResults = lastpoint.get(pic.hashCode());
					if (lastIfResults != null) {
						if (lastpoint.get(lastIfCode) == null) {
							lastpoint.put(lastIfCode, lastIfResults);
						} else {
							lastpoint.get(lastIfCode).addAll(lastIfResults);
						}
					}
				}
			} else {
				List<Integer> expressPreids = new ArrayList<>();
				if (i == 0) {
					// 获得ifcode 置换了括号的id
					int pid = b.getParent().hashCode();
					expressPreids.add(pid);
					mypic.setPreIds(expressPreids);
					mypic.label = label;
				} else {
					// 获得前一个语句的结果codelist
					ASTNode prepic = list.get(i - 1);
					// 大括号内当前语句的前置语句code
					int prepicCode = prepic.hashCode();
					// 获得前置语句的所有结果集
					expressPreids = getResultList(prepicCode, expressPreids);
					mypic.setPreIds(expressPreids);
				}
				addlink(mypic);
				// 添加当前大括号内最后一个语句,除掉return
				if (i == list.size() - 1 && !(pic instanceof ReturnStatement)) {
					lastpoint.get(lastIfCode).add(pic.hashCode());
				}
				if (pic instanceof ReturnStatement) {
					returnList.add(pic.hashCode());
				}

			}

		}
	}

	@Override
	public void preVisit(ASTNode n) {
		preids = new ArrayList<>();

		if (rootStateList.size() <= 0) {
			List<ASTNode> tempMapping = reversemapping.get(rootId);
			// List<ASTNode> changeMapping = new ArrayList<>();
			for (ASTNode s : tempMapping) {
				rootStateList.add(s.hashCode());
				// if (!(s instanceof IfStatement || s instanceof ForStatement || s instanceof
				// EnhancedForStatement)) {
				// rootStateList.add(s.hashCode());
				// changeMapping.add(s);
				// } else {
				// if (s instanceof ForStatement) {
				// Expression condition = ((ForStatement) s).getExpression();
				// rootStateList.add(condition.hashCode());
				// changeMapping.add(condition);
				// } else if (s instanceof EnhancedForStatement) {
				// Expression condition = ((EnhancedForStatement) s).getExpression();
				// rootStateList.add(condition.hashCode());
				// changeMapping.add(condition);
				// } else {
				// rootStateList.add(s.hashCode());
				// changeMapping.add(s);
				// }
				//
				// }
			}
			// 想重新定义主程序的序列（效果没达成）
			// reversemapping.put(rootId, changeMapping);
		}
		if (!(n instanceof IfStatement || n instanceof ForStatement || n instanceof EnhancedForStatement)) {
			int ownid = n.hashCode();
			// 自己大括号的hashcode
			int ppid = rootId;
			// 记录大括号内的最后语句定义

			if (rootStateList.contains(ownid)) {
				MyASTNode myNode = getNode(n);
				// 这是大括号内的所有语句
				if (reversemapping.get(ppid) != null) {
					List<Integer> rootpreids = new ArrayList<>();
					rootpreids = getPreids(reversemapping.get(ppid), ownid, ppid, rootpreids);
					myNode.setPreIds(rootpreids);
					addlink(myNode);
					if (lastpoint.get(ownid) == null) {

						List<Integer> formap = new ArrayList<>();
						formap.add(ownid);
						lastpoint.put(ownid, formap);
					}
				}
			}

		}
	}

	@Override
	public boolean visit(IfStatement n) {
		// n.getExpression().accept(this);
		// --------------------------------------------ifstatemnet----------------------------------------------
		// 自己的hashcode
		int ownid = n.hashCode();
		// 自己点的定义
		MyASTNode mypic = getNode(n);
		// mypic.label = n.getExpression().toString();
		// 自己队列的大括号的hashcode
		int ppid = n.getParent().hashCode();
		// 定义ifcode的最后语句定义，以if的code为key
		if (lastpoint.get(ownid) == null) {
			// 创建if的分支记录
			List<Integer> ifmap = new ArrayList<>();
			lastpoint.put(ownid, ifmap);
		}
		// 这是大括号内的所有语句(ppid有问题)if没有语句块
		;
		List<Integer> ifpreids = new ArrayList<>();
		if (reversemapping.get(ppid) != null) {
			// 完成多次置换
			ifpreids = getPreids(reversemapping.get(ppid), ownid, ppid, ifpreids);
			/***
			 * 之前思路 // //大括号的前置节点（第一次置换） // int pre = getPreid(reversemapping.get(ppid),
			 * ownid, ppid); // //大括号的前置节点有后置节点 // if (lastpoint.get(pre) != null) { //
			 * //（第二次置换） // preids = getlastPoint(pre, preids); // }else { //
			 * //大括号的前置节点没有后置节点，那大括号内的前置节点就是当前节点的前置节点 // preids.add(pre); // } // mypic.flag
			 * = getPreid(reversemapping.get(ppid), ownid, ppid);
			 ***/

		} else {
			// 大括号内没有前置语句
			ASTNode nparent = n.getParent();
			if (nparent instanceof Block) {
				int parentid = nparent.getParent().hashCode();
				ifpreids.add(parentid);
			} else {
				ifpreids.add(nparent.hashCode());
			}
			// preids.add(ppid);
			// mypic.flag = ppid;
		}
		mypic.setPreIds(ifpreids);
		// 当前节点 （归类节点）并且（创建连接）
		addlink(mypic);

		// --------------------------------------------if分支----------------------------------------------

		// 当前节点之后的分析
		// then
		Statement thenstatement = n.getThenStatement();
		// else
		Statement elsestatement = n.getElseStatement();

		if (thenstatement != null) {
			// 进入分支，记录分支ifcode
			stack.push(ownid);
			branch(thenstatement,"Yes");
		}
		if (null != elsestatement) {
			// 进入分支，记录分支ifcode
			stack.push(ownid);
			if (elsestatement instanceof IfStatement) {
				elsestatement.accept(this);
			} else {
				branch(elsestatement,"No");
			}
		} else {
			lastpoint.get(ownid).add(ownid);
		}

		return false;
	}

	/**
	 * 递归寻找所有的结果(针对if)
	 * 
	 * @param id
	 *            if的code为key
	 * @param preList
	 *            if 的结果list
	 * @return 返回if的结果list
	 */
	@Deprecated
	public List<Integer> getlastPoint(int id, List<Integer> preList) {
		List<Integer> recordList = lastpoint.get(id);// 结尾id
		// List<Integer> linkedList = canUseNodeMap.get(id).getPreIds();//前置id
		for (int preid : recordList) {
			if (lastpoint.get(preid) != null && lastpoint.get(preid).size() > 0) {
				if (preid == id) {// 拒绝死循环
					preList.add(preid);
				} else {
					preList = getlastPoint(preid, preList);
				}
			} else {
				preList.add(preid);
			}
		}
		// List<Integer> list = new ArrayList<>();
		// for (int p : preList) {
		// if (!linkedList.contains(p)) {
		// list.add(p);
		// }
		// }
		return preList;
	}

	@Override
	// 有问题，；临时占坑
	public boolean visit(TryStatement n) {
		ASTNode b = n.getBody();
		List<ASTNode> templist = new ArrayList<>();
		if (b instanceof Block) {
			templist = ((Block) b).statements();
		} else {
			templist.add(b);
		}
		for (ASTNode a : templist) {
			a.accept(this);
		}
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement n) {
		Expression condition = n.getExpression();// i <= 5条件菱形
		SingleVariableDeclaration tempParamenter = n.getParameter();


		MyASTNode conditionNode = getNode(condition);
		MyASTNode paramenterNode = getNode(tempParamenter);
		List<ASTNode> stateList = new ArrayList<>();
		stateList.add(condition);
		stateList.add(tempParamenter);


		int ownid = n.hashCode();
		// 自己大括号的hashcode
		int ppid = n.getParent().hashCode();
		// 记录大括号内的最后语句定义，以if的code为key
		List<Integer> temptest = lastpoint.get(ownid);
		int tempid = condition.hashCode();

		// enhancedFor id

		// 括号内的list顺序

			reversemapping.put(ownid, stateList);

		// 最后结果收集

			List<Integer> formap = new ArrayList<>();
			formap.add(condition.hashCode());
			formap.add(tempParamenter.hashCode());
			lastpoint.put(ownid, formap);
			exchangeId.put(ownid, tempid);


		List<MyASTNode> forList = new ArrayList<>();
		forList.add(conditionNode);
		forList.add(paramenterNode);
		// 记录for内的最后语句定义，以for的code为key,条件语句是最后一个




			MyASTNode prepic = conditionNode;
			MyASTNode pic = paramenterNode;
			int id = prepic.astNode.hashCode();
			List<Integer> picPreids = new ArrayList<>();
			picPreids = getNormalResultPoint(id, picPreids);
			pic.setPreIds(picPreids);
			addlink(pic);

		// 这是在enhanced之外的大括号内针对最后一个节点获得所有的前置节点，
		// 放最后的原因的必须循环内的节点结果也产生才能总结（特殊处理的主结点）
		if (reversemapping.get(ppid) != null) {
			List<Integer> forPreids = new ArrayList<>();
			// 针对block重新处理了
			forPreids = getNewPreids(reversemapping.get(ppid), n, forPreids);
			// 修正对于多种结果的回收
			forPreids = getResultList(forList.get(forList.size() - 1).astNode.hashCode(), forPreids);
			// 纯粹为了画图的操作
			conditionNode.setPreIds(forPreids);
			// conditionNode.shape = "invtriangle";
			addlink(conditionNode);
		}

		return false;
	}

	@Override
	public boolean visit(ForStatement n) {
		// ---------------------------for--------------------
		List<ASTNode> updater = n.updaters();// 递增i++最后一条语句
		List<ASTNode> initializer = n.initializers();// 初始化int i=0 开始第一条语句
		if (initializer.size() <= 0) {
			initializer = n.updaters();
		}
		Expression condition = n.getExpression();// i <= 5条件菱形
		if (condition == null) {
			System.out.println("/////////////////condition///////////////////");
			return false;
		}
		List<ASTNode> stateList = new ArrayList<>();

		stateList.add(condition);
		// 针对只有一个语句的时候
		if (!(n.getBody() instanceof Block)) {
			stateList.add(n.getBody());
		} else {
			Block body = (Block) n.getBody();
			stateList.addAll(body.statements());// 循环体
		}
		stateList.addAll(updater);
		List<ASTNode> stateListTemp = new ArrayList<>();
		stateListTemp.add(condition);
		stateListTemp.addAll(updater);
		int ownid = n.hashCode();
		// 自己大括号的hashcode
		int ppid = n.getParent().hashCode();
		if (stateList.size() >= 2) {
			ppid = stateList.get(1).getParent().hashCode();
		}

		// 括号的list顺序，刷新括号内顺序

		if (reversemapping.get(ppid) == null) { // 去掉了，感觉哪里赋值错误，已纠正，针对自己的括号内的赋值
			reversemapping.put(ppid, stateListTemp);
		}
		// 记录大括号内的最后语句定义，以if的code为key
		if (lastpoint.get(ownid) == null) {
			// 创建for循环记录
			List<Integer> formap = new ArrayList<>();
			formap.add(condition.hashCode());
			exchangeId.put(ownid, condition.hashCode());
			lastpoint.put(ownid, formap);
		}

		MyASTNode conditionNode = getNode(condition);
		conditionNode.shape = "circle";
		MyASTNode updaterNode = getNode(updater.get(0));

	

	

		// 针对初始化和条件做处理
		int parentid = n.getParent().hashCode();// 针对大环境的括号内的查询
		if (reversemapping.get(parentid) != null) {
			List<Integer> conditionPreIds = new ArrayList<>();
			// for所在大括号内的前置id
			conditionPreIds = getNewPreids(reversemapping.get(parentid), n, conditionPreIds);
			conditionNode.setPreIds(conditionPreIds);
			addlink(conditionNode);// 创建连接
			List<Integer> updaterPreIds = new ArrayList<>();
			updaterPreIds.add(condition.hashCode());
			updaterNode.setPreIds(updaterPreIds);
			addlink(updaterNode);// 创建连接
			lastpoint.get(ownid).add(updaterNode.astNode.hashCode());
		}

	

		

		// -------------------------------for循环体---------------
		// Statement stateList = n.getBody();

		return false;
	}

	/** 新方法 ***/
	public List<Integer> getNewPreids(List<ASTNode> ppids, ASTNode n, List<Integer> list) {
		int id = n.hashCode();
		// 寻找当前节点id属于哪个位置
		for (int i = 0; i < ppids.size(); i++) {
			if (ppids.get(i).hashCode() != id) {
				continue;
			}
			if (i == 0) {// 如果是第一个语句
				/**********
				 * 原方法 List<Integer> firstPids = new ArrayList<>(); firstPids.add(parentid);
				 * return firstPids;
				 ***********/
				ASTNode nparent = n.getParent();
				List<Integer> firstPids = new ArrayList<>();
				if (nparent instanceof Block) {
					int parentid = nparent.getParent().hashCode();
					firstPids.add(parentid);
				} else {
					firstPids.add(nparent.hashCode());
				}
				return firstPids;

			} else {
				// 获得主路上的前一个点id
				int ppid = ppids.get(i - 1).hashCode();
				// 针对id寻找他的结果集
				return getResultList(ppid, list);
			}
		}
		return list;
	}

	/** 废弃 **/
	@Deprecated
	public List<Integer> getForPreids(List<ASTNode> ppids, int id, int parentid, List<Integer> list) {
		// 寻找当前节点id属于哪个位置
		for (int i = 0; i < ppids.size(); i++) {
			if (ppids.get(i).hashCode() != id) {
				continue;
			}
			if (i == 0) {// 如果是第一个语句
				/**********
				 * 原方法 List<Integer> firstPids = new ArrayList<>(); firstPids.add(parentid);
				 * return firstPids;
				 ***********/

			} else {
				// 获得主路上的前一个点id
				int ppid = ppids.get(i - 1).hashCode();
				// 针对id寻找他的结果集
				return getResultList(ppid, list);
			}
		}
		return getResultList(parentid, list);
	}

	public List<Integer> getNormalResultPoint(int id, List<Integer> preList) {
		List<Integer> recordList = lastpoint.get(id);// 结尾id
		if (recordList == null || recordList.size() <= 0) {// 说明没有复杂操作
			preList.add(id);
		} else {
			for (int preid : recordList) {
				if (preid == id) {// 拒绝死循环
					preList.add(preid);
				} else {
					preList = getNormalResultPoint(preid, preList);
				}
			}
		}

		return preList;
	}

	@Override
	public boolean visit(SwitchStatement n) {
		Expression a = n.getExpression();
		List<ASTNode> blist = n.statements();
		return false;
	}

	@Override
	public void endVisit(MethodDeclaration node) {
		MyASTNode endNode = new MyASTNode();
		if (rootStateList.size() >= 1) {
			int lastId = reversemapping.get(rootId).get(rootStateList.size() - 1).hashCode();
			// 针对id寻找他的结果集
			List<Integer> endList = new ArrayList<>();
			endList = getResultList(lastId, endList);
			endList.addAll(returnList);
			endNode.setPreIds(endList);
			addlink(endNode);
		}
	}

	@Override
	public void endVisit(ReturnStatement node) {
		int lastId = node.hashCode();

	}

}
