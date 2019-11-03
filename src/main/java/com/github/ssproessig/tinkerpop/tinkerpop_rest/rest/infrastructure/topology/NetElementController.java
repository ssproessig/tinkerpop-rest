package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/infrastructure/topology/")
public class NetElementController {

  private final GraphService service;

  public NetElementController(GraphService service) {
    this.service = service;
  }

  @GetMapping(value = "net-elements")
  public List<NetElement> get() {
    val netElements = service.V().hasLabel("netElement");
    return netElements.toStream().map(NetElement::new).collect(Collectors.toList());
  }

}
