package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.val;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.structure.Vertex;


@Data
class NetElementPath {

  List<String> elements = new ArrayList<>();

  NetElementPath(Path p) {

    for (Object pp : p) {
      val v = (Vertex) pp;
      elements.add(v.property(Constants.EXT_ID).value().toString());
    }

  }

}
