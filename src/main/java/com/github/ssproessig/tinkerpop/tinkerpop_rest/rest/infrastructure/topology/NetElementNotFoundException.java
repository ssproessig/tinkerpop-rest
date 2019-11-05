package com.github.ssproessig.tinkerpop.tinkerpop_rest.rest.infrastructure.topology;


class NetElementNotFoundException extends RuntimeException {

  NetElementNotFoundException(String id) {
    super("NetElement not found: " + id);
  }

}
