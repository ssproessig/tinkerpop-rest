package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphDumper;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.functional_infrastructure.SwitchHandler;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology.NetElementHandler;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology.NetRelationHandler;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology.NetworkHandler;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology.NetworkLevelHandler;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.railML.infrastructure.topology.NetworkResourceHandler;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Slf4j
public class RailMLHandler extends DefaultHandler {
  private final TinkerGraph g;
  private BaseContext ctx;

  private List<BaseHandler> handlers =
      Arrays.asList(
          // /railML/infrastructure/topology
          new NetElementHandler(),
          new NetRelationHandler(),
          new NetworkHandler(),
          new NetworkLevelHandler(),
          new NetworkResourceHandler(),
          // /railML/infrastructure/topology
          new SwitchHandler());

  RailMLHandler(TinkerGraph graph) {
    g = graph;
    ctx = new BaseContext();

    handlers.forEach(h -> h.setReferences(g, ctx));
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {

    for (val handler : handlers) {
      if (handler.doesHandle(localName)) {
        handler.startElement(uri, localName, qName, attributes);
        break;
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {

    for (val handler : handlers) {
      if (handler.doesHandle(localName)) {
        handler.endElement(uri, localName, qName);
        break;
      }
    }
  }

  @Override
  public void endDocument() {
    GraphDumper.dumpTo(g, Constants.EXPORT_GRAPHML_TO);
  }
}
