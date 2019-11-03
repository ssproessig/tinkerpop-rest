package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;


import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import lombok.val;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;


public class RailMLLoader {

  public void loadTo(TinkerGraph graph)
      throws ParserConfigurationException, SAXException, IOException {

    val spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(true);

    val saxParser = spf.newSAXParser();

    val xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(new RailMLHandler(graph));

    val resource = new ClassPathResource("railML.org_SimpleExample_v11_railML3-1_04.xml");
    xmlReader.parse(resource.getFilename());
  }


}
