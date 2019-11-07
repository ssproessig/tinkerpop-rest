package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railml.infrastructure.functional_infrastructure;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphHelpers;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.BaseHandler;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.xml.sax.Attributes;

public class BufferStopHandler extends BaseHandler {
  private static final String ELEMENT_NAME = "bufferStop";
  private static final String[] ALSO_HANDLED_ELEMENTS = new String[] {"spotLocation"};

  private Vertex currentBufferStop;
  private String netElementRef;
  private String posOnElement;

  public BufferStopHandler() {
    super(ELEMENT_NAME);
  }

  @Override
  protected boolean doesHandle(String localName) {
    return ((localName.equals(ELEMENT_NAME))
        || (currentBufferStop != null && ArrayUtils.contains(ALSO_HANDLED_ELEMENTS, localName)));
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (ELEMENT_NAME.equals(localName)) {
      currentBufferStop = GraphHelpers.createVertex(g, localName, attributes.getValue("id"));
    }
    //
    else if ("spotLocation".equals(localName)) {
      netElementRef = attributes.getValue("netElementRef");
      posOnElement = attributes.getValue("pos");
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (ELEMENT_NAME.equals(localName)) {
      // FIXME: lacks error handling if using invalid reference
      val netRef = ctx.top.lookupNetworkResource(netElementRef);

      if (netRef.isPresent()) {
        float f = NumberUtils.toFloat(posOnElement, 0.0f);
        String connectToNetRef = (Float.compare(f, 0.0f) == 0) ? "beginsAt" : "endsAt";

        val netElementToTerminate =
            netRef.get().edges(Direction.OUT, connectToNetRef).next().inVertex();
        currentBufferStop.addEdge("terminates", netElementToTerminate);
      }

      currentBufferStop = null;
    }
  }
}
