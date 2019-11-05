package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.xml.sax.Attributes;


public class NetRelationHandler extends BaseHandler {

  private static final String ELEMENT_NAME = "netRelation";

  private static final String[] HANDLED_ELEMENTS = new String[]{
      ELEMENT_NAME, "elementA", "elementB"
  };

  public NetRelationHandler() {
    super(ELEMENT_NAME);
  }

  @Override
  protected boolean doesHandle(String localName) {
    return ArrayUtils.contains(HANDLED_ELEMENTS, localName);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (ELEMENT_NAME.equals(localName)) {
      val extId = attributes.getValue("id");

      ctx.currentNetRelation = GraphHelpers.createVertex(g, localName, extId);
      GraphHelpers.addPropertyFromAttributes(
          ctx.currentNetRelation, attributes, "navigability", "<no-navigability>");

      ctx.positionOnA = attributes.getValue("positionOnA");
      ctx.positionOnB = attributes.getValue("positionOnB");

      ctx.networkResources.put(extId, ctx.currentNetRelation);
    } else if (localName.startsWith("element") && ctx.currentNetRelation != null) {
      val refToLookup = attributes.getValue("ref") + "_" +
          ("elementA".equals(localName) ? "0" : "1");

      ctx.lookupNetworkResource(refToLookup).ifPresent(
          res -> {
            ctx.currentNetRelation.addEdge("connects", res);
            res.addEdge("connects", ctx.currentNetRelation);
          }
      );
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (ELEMENT_NAME.equals(localName)) {
      ctx.currentNetRelation = null;
    }
  }

}
