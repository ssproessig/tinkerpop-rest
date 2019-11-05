package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphService;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/infrastructure/topology/")
@Slf4j
public class NetElementPathsController {

  private final GraphService service;

  public NetElementPathsController(GraphService service) {
    this.service = service;
  }


  private Vertex getNetElement(String id) {
    return service.V().hasLabel("netElement").has("id", id).tryNext()
        .orElseThrow(() -> new NetElementNotFoundException(id));
  }


  @GetMapping(value = "net-element-paths", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<NetElementPath> get(
      @RequestParam @NotNull String fromNetElement,
      @RequestParam @NotNull String toNetElement
  ) {
    val fromVertex = getNetElement(fromNetElement);
    val toVertex = getNetElement(toNetElement);

    val before = Instant.now();

    List<Path> list = new ArrayList<>();

    service.V(fromVertex.id()).
        until(__.hasId(toVertex.id()).or().loops().is(100)).
        repeat(__.out().simplePath()).
        hasId(toVertex.id()).limit(2).path().fill(list);

    val after = Instant.now();

    log.info("Querying path(s) from '{}' to '{}' took {} ms",
        fromNetElement, toNetElement, Duration.between(before, after).toMillis());

    return list.stream().map(NetElementPath::new).collect(Collectors.toList());
  }

}
