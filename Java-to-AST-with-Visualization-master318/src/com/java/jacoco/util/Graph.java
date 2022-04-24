package com.java.jacoco.util;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.*;

public class Graph
{

    private HashMap<String, Vertex> vertexsMap; // key -name,value -vertex

    private HashMap<String, HashSet<String>> adjMap; // 

    public Graph()
    {
        adjMap = new HashMap<String, HashSet<String>>();
        vertexsMap = new HashMap<String, Vertex>();
    }

    public void delEdge(String start, String end)
    {
        adjMap.get(start).remove(end);
    }

    public void addEdge(String start, String end)
    {
        HashSet<String> set = adjMap.get(start);
        if (set == null)
            set = new HashSet<String>();
        set.add(end);
        adjMap.put(start, set);
    }

    public void addVertex(String vertexName,String code)
    {
        vertexsMap.put(vertexName, new Vertex(vertexName,code));
    }

    public boolean displayVertexVisited(String vertexName)
    {
        return vertexsMap.get(vertexName).WasVisited();
    }

    public HashSet<String> getAdjs(String vertexName)
    {
        return adjMap.get(vertexName);
    }

    public Vertex getVertexByName(String vertexName)
    {
        return vertexsMap.get(vertexName);

    }
}
