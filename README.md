# TinkerPop REST API
A sample implementation of 
- parsing a railML 3.1 file to a TinkerGraph
- and providing a REST API to query the graph again. 

## API provided
### Infrastructure/Topology
- `GET  /infrastructure/topology/networks`  to get all networks and their levels; use optional request parameter `withResources` to see connected `networkResources` as well
- `GET  /infrastructure/topology/net-elements`  to get all `netElements`
- `GET  /infrastructure/topology/net-element-path`  to find all possible paths in the graph between `fromNetElement` and `toNetElement` 

### Infrastructure/Functional Infrastructure
- `GET /infrastructure/functional-infrastructure/switches`  to get all switches

## Hints
### railML 3.1 used
You can use the environment variable `RAILML_TO_LOAD` to point the application to a local resource using `file:///...` schema.

If nothing is configured the application defaults to loading the official [railML 3.1 sample](https://svn.railml.org/railML3/tags/railML-3.1-final/examples/railML.org_SimpleExample_v11_railML3-1_04.xml).

### Graph created
The graph created from parsing the railML file is persisted to the temporary directory as GraphML file named `current_railML_import.graphml`. 

### Viewing the graph created
- Download [Gephi](https://gephi.org/) for your platform
- open the GraphML file created
- configure the visualization
  - __Layout__ tool window: use a layouting algorithm like `Yifan Hu Proportional`
  ![layouting settings](doc/gephi_layouting.png)
  - __Appearance__ tool window:
    - color __Nodes__ by configuring __Partition__ by `labelV`
    
      ![nodes appearance](doc/gephi_appearance_nodes.png)
      
    - color __Edges__ by configuring __Partition__ to `labelE`
    
      ![edges appearance](doc/gephi_appearance_edges.png)
      
  - configure __Graph view__
      - activate __Show Node labels__
      
        ![graph configuration](doc/gephi_graph_configuration.png)
        
      - use `extId` as __Label text__
      
        ![use extId as Label text](doc/gephi_label_text_settings.png)

- this results in the sample graph showing `/railML/infrastructure/topology` with the information
  - `network` called `nw01` with `network/level` called `lv0` (micro level),
  - highlighting some `netElement` entries prefixed with `ne_` and some `netRelation` prefixed with `nr_`
  
  ![sample graph](doc/gephi_sample_graph.png)
