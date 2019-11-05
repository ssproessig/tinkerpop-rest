package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;


import java.io.IOException;
import java.net.URL;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@UtilityClass
public class RailMLLoader {

  private SAXParser getSecureSAXParser()
      throws SAXException {

    val saxParser = new org.apache.xerces.parsers.SAXParser();

    // disallow DOCTYPE declaration
    saxParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    // do not include external general entities
    saxParser.setFeature("http://xml.org/sax/features/external-general-entities", false);
    // do not include external parameter entities or the external DTD subset
    saxParser.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    // do not include external DTDs
    saxParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

    return saxParser;
  }

  public static void loadFrom(URL url, TinkerGraph graph)
      throws SAXException, IOException {

    val saxParser = getSecureSAXParser();
    saxParser.setContentHandler(new RailMLHandler(graph));

    val inputSource = new InputSource(url.openStream());
    saxParser.parse(inputSource);

  }

}
