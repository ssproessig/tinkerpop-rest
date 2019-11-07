package com.github.ssproessig.tinkerpop.tinkerpop_rest.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
  public static final String RAILML_EXAMPLE_URL =
      "https://svn.railml.org/railML3/tags/railML-3.1-final/examples/railML.org_SimpleExample_v11_railML3-1_04.xml";

  public static final String ENV_TO_USE = "RAILML_TO_LOAD";

  public static final String EXPORT_GRAPHML_TO =
      System.getProperty("java.io.tmpdir") + "current_railML_import.graphml";

  public static final String EXT_ID = "extId";

  public static final String CONNECTS_EDGE = "connects";

  public static final String NETWORK_RESOURCE_EDGE = "networkResource";
}
