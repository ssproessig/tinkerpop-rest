package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import lombok.Data;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tinkerpop.gremlin.structure.Vertex;


@Data
class NetElement {

  private String id;
  private float length;

  NetElement(Vertex v) {
    id = v.property(Constants.EXT_ID).value().toString();
    length = NumberUtils.toFloat(v.property("length").value().toString(), -1.0f);
  }

}
