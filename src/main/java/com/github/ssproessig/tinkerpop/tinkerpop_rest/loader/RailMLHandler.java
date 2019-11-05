package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphDumper;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


@Slf4j
public class RailMLHandler extends DefaultHandler {

  private final TinkerGraph g;

  private Context ctx = new Context();

  RailMLHandler(TinkerGraph graph) {
    g = graph;
  }


  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    val extId = attributes.getValue("id");

    if ("netElement".equals(localName)) {
      val netElement = g.addVertex("netElement");
      netElement.property(Constants.EXT_ID, extId);
      GraphHelpers.addPropertyFromAttributes(netElement, attributes, "length", "");

      ctx.networkResources.put(extId, netElement);

      val netElementBegin = g.addVertex("netElementBegin");
      netElementBegin.property(Constants.EXT_ID, extId + "_0");
      ctx.networkResources.put(extId + "_0", netElementBegin);

      netElement.addEdge("beginsAt", netElementBegin);
      netElementBegin.addEdge("beginOf", netElement);

      val netElementEnd = g.addVertex("netElementEnd");
      netElementEnd.property(Constants.EXT_ID, extId);
      netElementEnd.property(Constants.EXT_ID, extId + "_1");
      ctx.networkResources.put(extId + "_1", netElementEnd);

      netElement.addEdge("endsAt", netElementEnd);
      netElementEnd.addEdge("endOf", netElement);

      return;
    }

    if ("netRelation".equals(localName)) {
      ctx.currentNetRelation = g.addVertex("netRelation");
      ctx.currentNetRelation.property(Constants.EXT_ID, extId);
      GraphHelpers.addPropertyFromAttributes(
          ctx.currentNetRelation, attributes, "navigability", "<no-navigability>");

      ctx.positionOnA = attributes.getValue("positionOnA");
      ctx.positionOnB = attributes.getValue("positionOnB");

      ctx.networkResources.put(extId, ctx.currentNetRelation);
      return;
    }

    if ("elementA".equals(localName) && ctx.currentNetRelation != null) {
      val ref = attributes.getValue("ref") + "_" + ctx.positionOnA;
      val res = ctx.networkResources.get(ref);

      if (res != null) {
        ctx.currentNetRelation.addEdge("connects", res);
        res.addEdge("connects", ctx.currentNetRelation);
      } else {
        log.error("missing networkResource '{}'", ref);
      }
    }

    if ("elementB".equals(localName) && ctx.currentNetRelation != null) {
      val ref = attributes.getValue("ref") + "_" + ctx.positionOnB;
      val res = ctx.networkResources.get(ref);

      if (res != null) {
        res.addEdge("connects", ctx.currentNetRelation);
        ctx.currentNetRelation.addEdge("connects", res);
      } else {
        log.error("missing networkResource '{}'", ref);
      }
    }

    if ("network".equals(localName)) {
      ctx.currentNetwork = g.addVertex("network");
      ctx.currentNetwork.property(Constants.EXT_ID, extId);
      return;
    }

    if ("level".equals(localName) && ctx.currentNetwork != null) {
      ctx.currentLevel = g.addVertex("level");
      ctx.currentLevel.property(Constants.EXT_ID, extId);
      GraphHelpers.addPropertyFromAttributes(
          ctx.currentLevel, attributes, "descriptionLevel", "<no-descriptionLevel>");

      ctx.currentNetwork.addEdge("level", ctx.currentLevel);
      return;
    }

    if ("networkResource".equals(localName) && ctx.currentLevel != null) {
      val ref = attributes.getValue("ref");
      val res = ctx.networkResources.get(ref);

      if (res != null) {
        ctx.currentLevel.addEdge("networkResource", res);
      } else {
        log.error("missing networkResource '{}'", ref);
      }
    }

  }

  @Override
  public void endElement(String uri, String localName, String qName) {

    if ("level".equals(localName)) {
      ctx.currentLevel = null;
    }

    if ("network".equals(localName)) {
      ctx.currentNetwork = null;
    }

    if ("netRelation".equals(localName)) {
      ctx.currentNetRelation = null;
    }

  }

  @Override
  public void endDocument() {
    GraphDumper.dumpTo(g, Constants.EXPORT_GRAPHML_TO);
  }

}
