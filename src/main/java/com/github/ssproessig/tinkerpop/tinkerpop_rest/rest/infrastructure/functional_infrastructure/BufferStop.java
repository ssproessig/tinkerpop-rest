package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.functional_infrastructure;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import lombok.Data;
import org.apache.tinkerpop.gremlin.structure.Vertex;

@Data
class BufferStop {
  private String id;
  private String type;

  BufferStop(Vertex v) {
    id = v.property(Constants.EXT_ID).value().toString();
    type = v.property("type").value().toString();
  }
}
