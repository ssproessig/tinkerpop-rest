package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import lombok.val;
import org.xml.sax.Attributes;

public class NetElementHandler extends BaseHandler {
  public NetElementHandler() {
    super("netElement");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    val extId = attributes.getValue("id");
    val netElement = GraphHelpers.createVertex(g, localName, extId);
    GraphHelpers.addPropertyFromAttributes(netElement, attributes, "length", "");
    ctx.top.networkResources.put(extId, netElement);

    val netElementBegin = GraphHelpers.createVertex(g, localName + "Begin", extId + "_0");
    ctx.top.networkResources.put(extId + "_0", netElementBegin);

    netElement.addEdge("beginsAt", netElementBegin);
    netElementBegin.addEdge("beginOf", netElement);

    val netElementEnd = GraphHelpers.createVertex(g, localName + "End", extId + "_1");
    ctx.top.networkResources.put(extId + "_1", netElementEnd);

    netElement.addEdge("endsAt", netElementEnd);
    netElementEnd.addEdge("endOf", netElement);
  }
}
