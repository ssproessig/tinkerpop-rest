package com.github.ssproessig.tinkerpop.tinkerpop_rest.graph;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.RailMLLoader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
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

  private static final String ENV_TO_USE = "RAILML_TO_LOAD";

  private TinkerGraph g;
  private GraphTraversalSource gts;

  public GraphService() {
    g = TinkerGraph.open();
    gts = g.traversal();

    String urlGiven = System.getenv(ENV_TO_USE);
    if (urlGiven == null) {
      urlGiven = "https://svn.railml.org/railML3/tags/railML-3.1-final/examples/railML.org_SimpleExample_v11_railML3-1_04.xml";
      log.info("Environment variable {} not set. Defaulting to railML example.", ENV_TO_USE);
    }

    try {
      URL url = new URL(urlGiven);

      log.info("Reading railML 3.1 from {}", urlGiven);
      RailMLLoader.loadFrom(url, g);
    } catch (MalformedURLException e) {
      log.error("Unable to derive url for {} given: {}", ENV_TO_USE, urlGiven);
      log.error(ExceptionUtils.getStackTrace(e));
      System.exit(1);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      log.error("Parsing '{}' failed:", urlGiven);
      log.error(ExceptionUtils.getStackTrace(e));
      System.exit(2);
    }
  }

  public GraphTraversal<Vertex, Vertex> V() {
    return gts.V();
  }

}
