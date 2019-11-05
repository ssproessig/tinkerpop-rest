package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

  @JsonInclude(Include.NON_EMPTY)
  private List<String> networkResources = new ArrayList<>();

  NetworkLevelInfo(Vertex v, boolean shallAddResources) {
    id = v.property(Constants.EXT_ID).value().toString();
    descriptionLevel = v.property("descriptionLevel").value().toString();

    if (shallAddResources) {
      v.vertices(Direction.BOTH, Constants.NETWORK_RESOURCE_EDGE).forEachRemaining(
          r -> networkResources.add(r.property(Constants.EXT_ID).value().toString())
      );
    }
  }
}


@Data
class Network {

  private String id;
  private List<NetworkLevelInfo> levels = new ArrayList<>();

  Network(Vertex v, boolean shallAddResources) {
    id = v.property(Constants.EXT_ID).value().toString();

    v.edges(Direction.BOTH, "level").forEachRemaining(
        edge -> levels.add(new NetworkLevelInfo(edge.inVertex(), shallAddResources))
    );
  }

}
