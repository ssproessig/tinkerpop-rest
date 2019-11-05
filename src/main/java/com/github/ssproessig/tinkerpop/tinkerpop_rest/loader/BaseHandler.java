package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.helpers.DefaultHandler;


public abstract class BaseHandler extends DefaultHandler {

  private String elementName;

  protected Context ctx;
  protected TinkerGraph g;

  protected BaseHandler(String elementNameToHandle) {
    elementName = elementNameToHandle;
  }

  void setReferences(TinkerGraph graphToUse, Context ctxToUse) {
    ctx = ctxToUse;
    g = graphToUse;
  }

  protected boolean doesHandle(String localName) {
    return (elementName.equals(localName));
  }

}
