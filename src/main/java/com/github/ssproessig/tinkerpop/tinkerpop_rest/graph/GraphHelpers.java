package com.github.ssproessig.tinkerpop.tinkerpop_rest.graph;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.Attributes;


@UtilityClass
public class GraphHelpers {

  public void addPropertyFromAttributes(Vertex v, Attributes a, String name, String defValue) {
    val value = a.getValue(name);

    // it is strongly discouraged to use "id" as property as it is up to the TinkerPop implementation
    // to use it itself internally — hence we rename the railML "id" to "extId" for externalId
    if ("id".equals(name)) {
      name = Constants.EXT_ID;
    }

    v.property(name, value != null ? value : defValue);
  }

  public Vertex createVertex(TinkerGraph g, String label, String extId) {
    val vertex = g.addVertex(label);
    vertex.property(Constants.EXT_ID, extId);
    return vertex;
  }

}
