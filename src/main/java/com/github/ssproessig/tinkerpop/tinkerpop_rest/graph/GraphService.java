package com.github.ssproessig.tinkerpop.tinkerpop_rest.graph;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.RailMLLoader;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;


@Service
public class GraphService {

  private TinkerGraph g;
  private GraphTraversalSource gts;

  public GraphService() {
    g = TinkerGraph.open();
    gts = g.traversal();

    RailMLLoader loader = new RailMLLoader();

    try {
      loader.loadTo(g);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
    }
  }

  public GraphTraversal<Vertex, Vertex> V() {
    return gts.V();
  }

}
