package com.github.ssproessig.tinkerpop.tinkerpop_rest.graph;

import java.io.FileOutputStream;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

@UtilityClass
@Slf4j
public class GraphDumper {
  public void dumpTo(TinkerGraph graph, String fileName) {
    try (val os = new FileOutputStream(fileName)) {
      log.info("Dumping graph {} as GraphML to: {}", graph, fileName);

      val w = GraphMLWriter.build().normalize(true).create();
      w.writeGraph(os, graph);

    } catch (IOException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
  }
}
