package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONIo;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


@Slf4j
public class RailMLHandler extends DefaultHandler {

  private final TinkerGraph g;

  RailMLHandler(TinkerGraph graph) {
    g = graph;
  }

  private void addPropertyFromAttributes(Vertex v, Attributes a, String name, String defValue) {
    val value = a.getValue(name);
    v.property(name, value != null ? value : defValue);
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    if ("netElement".equals(localName)) {
      val netElement = g.addVertex("netElement");
      addPropertyFromAttributes(netElement, attributes, "id", "<no-id>");
      addPropertyFromAttributes(netElement, attributes, "length", "");
    }

  }

  public void endDocument() {
    logGraph(g);
  }


  private void logGraph(TinkerGraph graph) {
    try {
      val os = new ByteArrayOutputStream();
      graph.io(GraphSONIo.build()).writer().create().writeGraph(os, graph);
      log.info(os.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
