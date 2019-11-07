package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railml.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import org.xml.sax.Attributes;

public class NetworkResourceHandler extends BaseHandler {
  public NetworkResourceHandler() {
    super(Constants.NETWORK_RESOURCE_EDGE);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    ctx.top
        .lookupNetworkResource(attributes.getValue("ref"))
        .ifPresent(res -> ctx.top.currentLevel.addEdge(localName, res));
  }
}
