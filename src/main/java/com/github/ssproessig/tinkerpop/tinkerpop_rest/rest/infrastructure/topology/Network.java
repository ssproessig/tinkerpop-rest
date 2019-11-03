package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;


@Data
class NetworkLevelInfo {

  private String id;
  private String descriptionLevel;

  NetworkLevelInfo(Vertex v) {
    id = v.property("id").value().toString();
    descriptionLevel = v.property("descriptionLevel").value().toString();
  }
}


@Data
class Network {

  private String id;
  private List<NetworkLevelInfo> levels = new ArrayList<>();

  Network(Vertex v) {
    id = v.property("id").value().toString();

    v.edges(Direction.BOTH, "level").forEachRemaining(
        edge -> levels.add(new NetworkLevelInfo(edge.inVertex()))
    );
  }

}
