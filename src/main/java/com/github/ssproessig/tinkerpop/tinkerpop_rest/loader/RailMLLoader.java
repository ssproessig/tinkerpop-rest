package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;


import java.io.IOException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@UtilityClass
public class RailMLLoader {

  public static void loadFrom(URL url, TinkerGraph graph)
      throws ParserConfigurationException, SAXException, IOException {

    val spf = SAXParserFactory.newInstance();
    spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    spf.setNamespaceAware(true);

    val saxParser = spf.newSAXParser();

    val xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(new RailMLHandler(graph));

    val inputSource = new InputSource(url.openStream());
    xmlReader.parse(inputSource);
  }

}
