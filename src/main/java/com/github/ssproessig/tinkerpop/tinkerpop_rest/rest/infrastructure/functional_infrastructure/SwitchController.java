package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.functional_infrastructure;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.graph.GraphService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/infrastructure/functional-infrastructure/")
public class SwitchController {

  private final GraphService service;

  public SwitchController(GraphService service) {
    this.service = service;
  }

  @GetMapping(value = "switches")
  public List<Switch> get() {
    val before = Instant.now();
    val switches = service.V().hasLabel("switch");
    val after = Instant.now();
    log.info("Querying switches from took {} ms", Duration.between(before, after).toMillis());

    return switches.toStream().map(Switch::new).collect(Collectors.toList());
  }
}
