package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import org.xml.sax.Attributes;


public class NetworkResourceHandler extends BaseHandler {

  public NetworkResourceHandler() {
    super("networkResource");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    ctx.lookupNetworkResource(attributes.getValue("ref")).ifPresent(
        res -> ctx.currentLevel.addEdge(localName, res)
    );
  }

}
