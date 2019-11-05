package com.github.ssproessig.tinkerpop.tinkerpop_rest.graph;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.RailMLLoader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;


@Service
@Slf4j
public class GraphService {

  private TinkerGraph g;
  private GraphTraversalSource gts;

  public GraphService() {
    g = TinkerGraph.open();
    gts = g.traversal();

    String urlGiven = System.getenv(Constants.ENV_TO_USE);
    if (urlGiven == null) {
      urlGiven = Constants.RAILML_EXAMPLE_URL;
      log.info("Environment variable {} not set. Defaulting to railML example.",
          Constants.ENV_TO_USE);
    }

    try {
      URL url = new URL(urlGiven);

      log.info("Reading railML 3.1 from {}", urlGiven);
      RailMLLoader.loadFrom(url, g);
    } catch (MalformedURLException e) {
      log.error("Unable to derive url for {} given: {}", Constants.ENV_TO_USE, urlGiven);
      log.error(ExceptionUtils.getStackTrace(e));
      System.exit(1);
    } catch (IOException | SAXException e) {
      log.error("Parsing '{}' failed:", urlGiven);
      log.error(ExceptionUtils.getStackTrace(e));
      System.exit(2);
    }
  }

  public GraphTraversal<Vertex, Vertex> V(final Object... vertexIds) {
    return gts.V(vertexIds);
  }

}
