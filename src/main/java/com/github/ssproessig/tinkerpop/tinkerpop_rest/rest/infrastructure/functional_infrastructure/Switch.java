package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.functional_infrastructure;

import com.github.ssproessig.tinkerpop.tinkerpop_rest.config.Constants;
import com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.functional_infrastructure.SwitchBranch.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.tinkerpop.gremlin.structure.Vertex;

@Data
class Switch {
  private String type;
  private String id;
  private List<SwitchBranch> branches = new ArrayList<>();

  Switch(Vertex v) {
    type = "Point";
    id = v.property(Constants.EXT_ID).value().toString();
    branches.add(new SwitchBranch(Type.TIP, v));
    branches.add(new SwitchBranch(Type.LEFT, v));
    branches.add(new SwitchBranch(Type.RIGHT, v));
  }
}
