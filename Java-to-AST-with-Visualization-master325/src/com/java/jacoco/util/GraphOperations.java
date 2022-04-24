package com.java.jacoco.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
/**
 * 获得所有路径
 * @author shaojing
 *
 */
public class GraphOperations {

	Graph graph;

	public GraphOperations(Graph graph) {
		this.graph = graph;
	}

	public List<LinkedList<Vertex>> getResult(String start, String end) {

		if (!isConnectable(start, end)) {

			return null;
		} else {

			return getConnPaths(start, end);
		}
	}

	private List<LinkedList<Vertex>> getConnPaths(String start, String end) {
		graph.getVertexByName(start).setWasVisited(true); // mark it
		LinkedList<String> stack = new LinkedList<String>();// list use as a
		List<LinkedList<Vertex>> result = new LinkedList<LinkedList<Vertex>>();
		stack.addFirst(start);

		while (!stack.isEmpty()) {
			String v = getAdjUnvisitedVertex(stack.peek(), stack);
			if (v == null) // if no such vertex,已经做了修改
			{
				HashMap<String, Boolean> clearVisitedMap = new HashMap<String, Boolean>();
                graph.getVertexByName(stack.peek()).setAllVisitedMap(clearVisitedMap);
				stack.removeFirst();
			} else
			// if it exists,
			{
				stack.addFirst(v); // push it
			}

			if (!stack.isEmpty() && end.equals(stack.peek())) {
				graph.getVertexByName(end).setWasVisited(false); // mark it
			//临时去掉
				result.add(getPathByStack(stack));
				stack.removeFirst();
			}
		}

		return result;
	}

	
	private boolean isConnectable(String start, String end) {
		ArrayList<String> queue = new ArrayList<String>();
		HashSet<String> visited = new HashSet<String>();
		queue.add(start);
		while (!queue.isEmpty()) {
			HashSet<String> adjNames = graph.getAdjs(start);
			for (String adjName : adjNames) {
				if (!visited.contains(adjNames)) {
					queue.add(adjName);
				}
			}
			if (queue.contains(end)) {
				return true;
			} else {
				visited.add(queue.get(0));
				queue.remove(0);
				if (!queue.isEmpty()) {
					start = queue.get(0);
				}
			}
		}
		return false;
	}

/**
 * 已经做了修改
 * @param v
 * @param stack
 * @return
 */
	public String getAdjUnvisitedVertex(String v, LinkedList<String> stack) {

		HashSet<String> adjNamesSet = graph.getAdjs(v);
		for (String adjName : adjNamesSet) {
			if (!graph.getVertexByName(v).getVisited(adjName)&&!stack.contains(adjName)) {
				graph.getVertexByName(v).setVisited(adjName);
				return adjName;
			}

		}
		return null;
	}


	public  LinkedList<Vertex>getPathByStack(LinkedList<String> stack) {
		LinkedList<Vertex> result = new LinkedList<Vertex>();
		List<String>  routes = new ArrayList<>();
		String popstring = "";
		for (int i = stack.size() - 1; i >= 0; i--) {
			
			if (popstring.isEmpty()) {
				popstring = "\"" + stack.get(i) + "\"";
			} else {
				popstring += "->" + "\"" + stack.get(i) + "\"";
			}
			int j = stack.size() -2;
			if(j>=0) {
				String phashcode = stack.get(i);
				String hashcode = stack.get(j);
				
			}
			result.add(graph.getVertexByName(stack.get(i)));
		}
		routes.add(popstring);
		System.out.println(popstring);
		return result;
	}

}
