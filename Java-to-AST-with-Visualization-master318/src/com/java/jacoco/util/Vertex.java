package com.java.jacoco.util;
import java.util.HashMap;
import java.util.HashSet;

public class Vertex
{

    boolean wasVisited;

    public String vertexName; 
    public String vertexCode;

    HashMap<String, Boolean> allVisitedMap;//点 中访问的路径，写的真好

    public HashMap<String, Boolean> getAllVisitedMap()
    {
        return allVisitedMap;
    }

    public void setAllVisitedMap(HashMap<String, Boolean> allVisitedMap)
    {
        this.allVisitedMap = allVisitedMap;
    }

    public boolean isWasVisited()
    {
        return wasVisited;
    }

    public boolean WasVisited()
    {
        return wasVisited;
    }

    public void setWasVisited(boolean wasVisited)
    {
        this.wasVisited = wasVisited;
    }

    public String getVertexName()
    {
        return vertexName;
    }
    public String getVertexCode()
    {
        return vertexCode;
    }

    public void setVertexName(String vertexName)
    {
        this.vertexName = vertexName;
    }

    public Vertex(String vertexName,String code) // constructor
    {
        allVisitedMap = new HashMap<String, Boolean>();
        this.vertexName = vertexName;
        this.vertexCode = code;
        wasVisited = false;
    }

    public void setVisited(String vertexName)
    {
        allVisitedMap.put(vertexName, true);

    }

    public boolean getVisited(String vertexName)
    {
        if (allVisitedMap.get(vertexName) == null)
            return false;
        else if (allVisitedMap.get(vertexName) == true)
            return true;
        else
            return false;
    }
    public void clearVisitedLog(){
        allVisitedMap=new HashMap<String,Boolean>();
    }
}
