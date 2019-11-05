package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphDumper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


@Slf4j
public class RailMLHandler extends DefaultHandler {

  private final TinkerGraph g;

  private Vertex currentNetwork;
  private Vertex currentLevel;
  private Map<String, Vertex> networkResources = new HashMap<>();


  RailMLHandler(TinkerGraph graph) {
    g = graph;
  }

  private void addPropertyFromAttributes(Vertex v, Attributes a, String name, String defValue) {
    val value = a.getValue(name);
    v.property(name, value != null ? value : defValue);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    if ("netElement".equals(localName)) {
      val id = attributes.getValue("id");
      val netElement = g.addVertex("netElement");
      addPropertyFromAttributes(netElement, attributes, "id", "<no-id>");
      addPropertyFromAttributes(netElement, attributes, "length", "");

      networkResources.put(id, netElement);

      val netElementBegin = g.addVertex("netElementBegin");
      addPropertyFromAttributes(netElementBegin, attributes, "id", "<no-id>");
      netElementBegin.property("id", id + "_0");
      networkResources.put(id + "_0", netElementBegin);

      netElement.addEdge("beginsAt", netElementBegin);
      netElementBegin.addEdge("beginOf", netElement);

      val netElementEnd = g.addVertex("netElementEnd");
      addPropertyFromAttributes(netElementEnd, attributes, "id", "<no-id>");
      netElementEnd.property("id", id + "_1");
      networkResources.put(id + "_1", netElementEnd);

      netElement.addEdge("endsAt", netElementEnd);
      netElementEnd.addEdge("endOf", netElement);

      return;
    }

    if ("netRelation".equals(localName)) {
      val netRelation = g.addVertex("netRelation");
      addPropertyFromAttributes(netRelation, attributes, "id", "<no-id>");
      return;
    }

    if ("network".equals(localName)) {
      currentNetwork = g.addVertex("network");
      addPropertyFromAttributes(currentNetwork, attributes, "id", "<no-id>");
      return;
    }

    if ("level".equals(localName) && currentNetwork != null) {
      currentLevel = g.addVertex("level");
      addPropertyFromAttributes(currentLevel, attributes, "id", "<no-id>");
      addPropertyFromAttributes(currentLevel, attributes, "descriptionLevel", "<no>");

      currentNetwork.addEdge("level", currentLevel);
      return;
    }

    if ("networkResource".equals(localName) && currentLevel != null) {
      val ref = attributes.getValue("ref");
      val res = networkResources.get(ref);

      if (res != null) {
        currentLevel.addEdge("networkResource", res);
      } else {
        log.error("missing networkResource '{}'", ref);
      }
    }

  }

  @Override
  public void endElement(String uri, String localName, String qName) {

    if ("level".equals(localName)) {
      currentLevel = null;
    }

    if ("network".equals(localName)) {
      currentNetwork = null;
    }

  }

  @Override
  public void endDocument() {
    GraphDumper.dumpTo(g, Constants.EXPORT_GRAPHML_TO);
  }

}
