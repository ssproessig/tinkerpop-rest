package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railml.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.xml.sax.Attributes;

public class NetRelationHandler extends BaseHandler {
  private static final String ELEMENT_NAME = "netRelation";

  private static final String[] HANDLED_ELEMENTS =
      new String[] {ELEMENT_NAME, "elementA", "elementB"};

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

      ctx.top.currentNetRelation = GraphHelpers.createVertex(g, localName, extId);
      GraphHelpers.addPropertyFromAttributes(
          ctx.top.currentNetRelation, attributes, "navigability", "<no-navigability>");

      ctx.top.positionOnA = attributes.getValue("positionOnA");
      ctx.top.positionOnB = attributes.getValue("positionOnB");

      ctx.top.networkResources.put(extId, ctx.top.currentNetRelation);
    } else if (localName.startsWith("element") && ctx.top.currentNetRelation != null) {
      val refToLookup =
          attributes.getValue("ref") + "_" + ("elementA".equals(localName) ? "0" : "1");

      ctx.top
          .lookupNetworkResource(refToLookup)
          .ifPresent(
              res -> {
                ctx.top.currentNetRelation.addEdge(Constants.CONNECTS_EDGE, res);
                res.addEdge(Constants.CONNECTS_EDGE, ctx.top.currentNetRelation);
              });
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (ELEMENT_NAME.equals(localName)) {
      ctx.top.currentNetRelation = null;
    }
  }
}
