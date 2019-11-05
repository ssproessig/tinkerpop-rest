package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
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
    id = v.property(Constants.EXT_ID).value().toString();
    descriptionLevel = v.property("descriptionLevel").value().toString();
  }
}


@Data
class Network {

  private String id;
  private List<NetworkLevelInfo> levels = new ArrayList<>();

  Network(Vertex v) {
    id = v.property(Constants.EXT_ID).value().toString();

    v.edges(Direction.BOTH, "level").forEachRemaining(
        edge -> levels.add(new NetworkLevelInfo(edge.inVertex()))
    );
  }

}
