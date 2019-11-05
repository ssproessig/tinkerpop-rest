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

  private Vertex currentNetRelation;
  private String positionOnA;
  private String positionOnB;

  private Map<String, Vertex> networkResources = new HashMap<>();


  RailMLHandler(TinkerGraph graph) {
    g = graph;
  }

  private void addPropertyFromAttributes(Vertex v, Attributes a, String name, String defValue) {
    val value = a.getValue(name);

    // it is strongly discouraged to use "id" as property as it is up to the TinkerPop implementation
    // to use it itself internally — hence we rename the railML "id" to "extId" for externalId
    if ("id".equals(name)) {
      name = Constants.EXT_ID;
    }

    v.property(name, value != null ? value : defValue);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    val extId = attributes.getValue("id");

    if ("netElement".equals(localName)) {
      val netElement = g.addVertex("netElement");
      netElement.property(Constants.EXT_ID, extId);
      addPropertyFromAttributes(netElement, attributes, "length", "");

      networkResources.put(extId, netElement);

      val netElementBegin = g.addVertex("netElementBegin");
      netElementBegin.property(Constants.EXT_ID, extId + "_0");
      networkResources.put(extId + "_0", netElementBegin);

      netElement.addEdge("beginsAt", netElementBegin);
      netElementBegin.addEdge("beginOf", netElement);

      val netElementEnd = g.addVertex("netElementEnd");
      netElementEnd.property(Constants.EXT_ID, extId);
      netElementEnd.property(Constants.EXT_ID, extId + "_1");
      networkResources.put(extId + "_1", netElementEnd);

      netElement.addEdge("endsAt", netElementEnd);
      netElementEnd.addEdge("endOf", netElement);

      return;
    }

    if ("netRelation".equals(localName)) {
      currentNetRelation = g.addVertex("netRelation");
      currentNetRelation.property(Constants.EXT_ID, extId);
      addPropertyFromAttributes(currentNetRelation, attributes, "navigability",
          "<no-navigability>");

      positionOnA = attributes.getValue("positionOnA");
      positionOnB = attributes.getValue("positionOnB");

      networkResources.put(extId, currentNetRelation);
      return;
    }

    if ("elementA".equals(localName) && currentNetRelation != null) {
      val ref = attributes.getValue("ref") + "_" + positionOnA;
      val res = networkResources.get(ref);

      if (res != null) {
        currentNetRelation.addEdge("connects", res);
        res.addEdge("connects", currentNetRelation);
      } else {
        log.error("missing networkResource '{}'", ref);
      }
    }

    if ("elementB".equals(localName) && currentNetRelation != null) {
      val ref = attributes.getValue("ref") + "_" + positionOnB;
      val res = networkResources.get(ref);

      if (res != null) {
        res.addEdge("connects", currentNetRelation);
        currentNetRelation.addEdge("connects", res);
      } else {
        log.error("missing networkResource '{}'", ref);
      }
    }

    if ("network".equals(localName)) {
      currentNetwork = g.addVertex("network");
      currentNetwork.property(Constants.EXT_ID, extId);
      return;
    }

    if ("level".equals(localName) && currentNetwork != null) {
      currentLevel = g.addVertex("level");
      currentLevel.property(Constants.EXT_ID, extId);
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

    if ("netRelation".equals(localName)) {
      currentNetRelation = null;
    }

  }

  @Override
  public void endDocument() {
    GraphDumper.dumpTo(g, Constants.EXPORT_GRAPHML_TO);
  }

}
