package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.functional_infrastructure;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import lombok.Data;
import lombok.val;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;


@Data
class SwitchBranch {

  enum Type {
    TIP,
    LEFT,
    RIGHT
  }

  private Type branchType;
  private String neighbour;

  SwitchBranch(Type type, Vertex v) {
    branchType = type;

    val edgeLabel = type.toString().toLowerCase();
    neighbour = v.edges(Direction.OUT, edgeLabel).next()
        .inVertex().property(Constants.EXT_ID).value().toString();
  }
}
