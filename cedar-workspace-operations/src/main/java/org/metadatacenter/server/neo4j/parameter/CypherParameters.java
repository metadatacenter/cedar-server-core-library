package org.metadatacenter.server.neo4j.parameter;

import org.metadatacenter.server.neo4j.cypher.CypherQueryParameter;

import java.util.HashMap;

public class CypherParameters extends HashMap<CypherQueryParameter, Object> {
/*  public Map<String, Object> asMap() {
    Map<String, Object> r = new HashMap();
    for (CypherQueryParameter k : this.keySet()) {
      r.put(k.getValue(), this.get(k));
    }
    return r;
  }*/
}
