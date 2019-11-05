package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import java.util.HashMap;
import java.util.Map;
import org.apache.tinkerpop.gremlin.structure.Vertex;


public class Context {

  public Map<String, Vertex> networkResources = new HashMap<>();

  public Vertex currentNetwork;
  public Vertex currentLevel;

  public Vertex currentNetRelation;
  public String positionOnA;
  public String positionOnB;

}
