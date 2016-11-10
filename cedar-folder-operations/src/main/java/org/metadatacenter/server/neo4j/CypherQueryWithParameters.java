package org.metadatacenter.server.neo4j;

import java.util.Map;

public class CypherQueryWithParameters implements CypherQuery {
  private final String query;
  private final Map<String, Object> parameters;

  public CypherQueryWithParameters(String query, Map<String, Object> parameters) {
    this.query = query;
    this.parameters = parameters;
  }

  @Override
  public String getQuery() {
    return query;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public String getLiteralCypher() {
    String q = this.query;
    if (q != null) {
      for (String key : parameters.keySet()) {
        Object o = parameters.get(key);
        String v;
        if (o == null) {
          v = "null";
        } else {
          v = "\"" + o.toString().replace("\"", "\\\"") + "\"";
        }
        q = q.replace("{" + key + "}", v);
      }
    }
    return q;
  }
}
