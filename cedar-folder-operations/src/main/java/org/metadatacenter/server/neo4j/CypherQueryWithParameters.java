package org.metadatacenter.server.neo4j;

import java.util.Map;

public class CypherQueryWithParameters implements CypherQuery {
  private String query;
  private Map<String, Object> parameters;
  private String literalCypher;

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
    for (String key : parameters.keySet()) {
      Object o = parameters.get(key);
      String v = null;
      if (o != null) {
        v = "\"" + o.toString().replace("\"", "\\\"") + "\"";
      }
      q = q.replace("{" + key + "}", v);
    }
    return q;
  }
}
