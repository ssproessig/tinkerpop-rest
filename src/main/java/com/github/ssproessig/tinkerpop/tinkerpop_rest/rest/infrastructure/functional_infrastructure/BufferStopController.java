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
public class BufferStopController {
  private final GraphService service;

  public BufferStopController(GraphService service) {
    this.service = service;
  }

  @GetMapping(value = "buffer-stops")
  public List<BufferStop> get() {
    val before = Instant.now();
    val bufferStops = service.V().hasLabel("bufferStop");
    val after = Instant.now();
    log.info("Querying bufferStops from took {} ms", Duration.between(before, after).toMillis());

    return bufferStops.toStream().map(BufferStop::new).collect(Collectors.toList());
  }
}
