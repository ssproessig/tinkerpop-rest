package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import org.xml.sax.Attributes;

public class NetworkLevelHandler extends BaseHandler {
  public NetworkLevelHandler() {
    super("level");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    ctx.top.currentLevel = GraphHelpers.createVertex(g, localName, attributes.getValue("id"));

    GraphHelpers.addPropertyFromAttributes(
        ctx.top.currentLevel, attributes, "descriptionLevel", "<no-descriptionLevel>");

    ctx.top.currentNetwork.addEdge(localName, ctx.top.currentLevel);
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    ctx.top.currentLevel = null;
  }
}
