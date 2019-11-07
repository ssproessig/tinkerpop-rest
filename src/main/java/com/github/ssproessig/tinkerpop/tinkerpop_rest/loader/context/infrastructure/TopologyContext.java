package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.context.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tinkerpop.gremlin.structure.Vertex;

@Slf4j
public class TopologyContext {
  public Map<String, Vertex> networkResources = new HashMap<>();

  public Vertex currentNetwork;
  public Vertex currentLevel;

  public Vertex currentNetRelation;
  public String positionOnA;
  public String positionOnB;

  public Optional<Vertex> lookupNetworkResource(String ref) {
    val res = networkResources.get(ref);

    if (res == null) {
      log.error("missing networkResource '{}'", ref);
    }

    return Optional.ofNullable(res);
  }
}
