package com.github.ssproessig.tinkerpop.tinkerpop_rest.graph;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.Attributes;

@UtilityClass
public class GraphHelpers {
  public void addPropertyFromAttributes(Vertex v, Attributes a, String name, String defValue) {
    val value = a.getValue(name);
    v.property(name, value != null ? value : defValue);
  }

  public Vertex createVertex(TinkerGraph g, String label, String extId) {
    val vertex = g.addVertex(label);
    vertex.property(Constants.EXT_ID, extId);
    return vertex;
  }

  public Vertex dissolveRelation(Vertex source, Vertex relation) {
    val edges = relation.edges(Direction.IN, Constants.CONNECTS_EDGE);

    while (edges.hasNext()) {
      val v = edges.next().outVertex();
      if (v != source) {
        return v;
      }
    }

    return null;
  }
}
