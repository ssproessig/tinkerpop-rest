package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import org.xml.sax.Attributes;


public class NetworkHandler extends BaseHandler {

  public NetworkHandler() {
    super("network");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    ctx.top.currentNetwork = GraphHelpers.createVertex(g, localName, attributes.getValue("id"));
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    ctx.top.currentNetwork = null;
  }
}
