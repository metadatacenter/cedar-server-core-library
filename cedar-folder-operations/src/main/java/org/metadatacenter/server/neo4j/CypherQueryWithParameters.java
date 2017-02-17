package org.metadatacenter.server.neo4j;

import java.util.List;
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

  @Override
  public String getFlatQuery() {
    if (query != null) {
      return query.replace("\n", " ").replace("\r", " ");
    } else {
      return null;
    }
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public String getLiteralCypher() {
    String q = this.query;
    if (q != null) {
      for (String key : parameters.keySet()) {
        Object o = parameters.get(key);
        String v = getVariableRepresentation(o);
        q = q.replace("{" + key + "}", v);
      }
    }
    return q.replace("\n", "").replace("\r", "");
  }

  private String getVariableRepresentation(Object o) {
    StringBuilder sb = new StringBuilder();
    if (o == null) {
      sb.append("null");
    } else if (o instanceof String) {
      sb.append("\"").append(((String) o).replace("\"", "\\\"")).append("\"");
    } else if (o instanceof Integer) {
      sb.append(String.valueOf((Integer) o));
    } else if (o instanceof List) {
      sb.append("[");
      List l = (List) o;
      String separator = "";
      for (Object li : l) {
        sb.append(separator);
        sb.append(getVariableRepresentation(li));
        separator = ", ";
      }
      sb.append("]");
    }
    return sb.toString();
  }
}
