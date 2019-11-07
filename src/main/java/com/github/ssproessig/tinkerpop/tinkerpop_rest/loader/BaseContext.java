package com.github.ssproessig.tinkerpop.tinkerpop_rest.loader;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.context.infrastructure.FunctionalInfrastructureContext;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.loader.context.infrastructure.TopologyContext;

public class BaseContext {
  public FunctionalInfrastructureContext fi = new FunctionalInfrastructureContext();

  public TopologyContext top = new TopologyContext();
}
