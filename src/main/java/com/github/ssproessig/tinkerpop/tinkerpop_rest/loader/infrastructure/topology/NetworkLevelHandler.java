package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import org.xml.sax.Attributes;


public class NetworkLevelHandler extends BaseHandler {

  public NetworkLevelHandler() {
    super("level");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    ctx.currentLevel = GraphHelpers.createVertex(g, localName, attributes.getValue("id"));

    GraphHelpers.addPropertyFromAttributes(
        ctx.currentLevel, attributes, "descriptionLevel", "<no-descriptionLevel>");

    ctx.currentNetwork.addEdge(localName, ctx.currentLevel);
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    ctx.currentLevel = null;
  }

}
