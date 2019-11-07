package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.functional_infrastructure;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.xml.sax.Attributes;

public class SwitchHandler extends BaseHandler {
  private static final String ELEMENT_NAME = "switchIS";
  private static final String[] ALSO_HANDLED_ELEMENTS =
      new String[] {"spotLocation", "leftBranch", "rightBranch"};

  public SwitchHandler() {
    super(ELEMENT_NAME);
  }

  @Override
  protected boolean doesHandle(String localName) {
    return ((localName.equals(ELEMENT_NAME))
        || (ctx.fi.currentSwitch != null && ArrayUtils.contains(ALSO_HANDLED_ELEMENTS, localName)));
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (ELEMENT_NAME.equals(localName)) {
      ctx.fi.currentSwitch = GraphHelpers.createVertex(g, "switch", attributes.getValue("id"));
    }
    //
    else if ("spotLocation".equals(localName)) {
      ctx.fi.switchTipRef = attributes.getValue("netElementRef");
      ctx.fi.tipPosOnNetElement = attributes.getValue("pos");
    }
    //
    else if ("leftBranch".equals(localName)) {
      ctx.fi.switchLeftRelationRef = attributes.getValue("netRelationRef");
    }
    //
    else if ("rightBranch".equals(localName)) {
      ctx.fi.switchRightRelationRef = attributes.getValue("netRelationRef");
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (ELEMENT_NAME.equals(localName)) {
      // FIXME: lacks error handling if using invalid reference
      val tipRef = ctx.top.lookupNetworkResource(ctx.fi.switchTipRef);
      val leftRelRef = ctx.top.lookupNetworkResource(ctx.fi.switchLeftRelationRef);
      val rightRelRef = ctx.top.lookupNetworkResource(ctx.fi.switchRightRelationRef);

      if (tipRef.isPresent() && leftRelRef.isPresent() && rightRelRef.isPresent()) {
        float f = NumberUtils.toFloat(ctx.fi.tipPosOnNetElement, 0.0f);
        String connectTipTo = (Float.compare(f, 0.0f) == 0) ? "beginsAt" : "endsAt";

        val tip = tipRef.get().edges(Direction.OUT, connectTipTo).next().inVertex();
        ctx.fi.currentSwitch.addEdge("tip", tip);

        val left = GraphHelpers.dissolveRelation(tip, leftRelRef.get());
        ctx.fi.currentSwitch.addEdge("left", left);

        val right = GraphHelpers.dissolveRelation(tip, rightRelRef.get());
        ctx.fi.currentSwitch.addEdge("right", right);
      }

      ctx.fi.currentSwitch = null;
    }
  }
}
