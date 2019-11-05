package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/infrastructure/topology/")
public class NetworkController {

  private final GraphService service;

  public NetworkController(GraphService service) {
    this.service = service;
  }

  @GetMapping(value = "networks")
  public List<Network> get(@RequestParam(required = false) String withResources) {
    val netElements = service.V().hasLabel("network");
    return netElements.toStream().map(
        e -> new Network(e, withResources != null)
    ).collect(Collectors.toList());
  }

}
