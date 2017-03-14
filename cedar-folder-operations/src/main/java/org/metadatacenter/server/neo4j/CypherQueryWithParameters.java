package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.CypherQueryParameter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CypherQueryWithParameters extends AbstractCypherQuery {

  private final CypherParameters parameters;

  public CypherQueryWithParameters(String query, CypherParameters parameters) {
    super(query);
    this.parameters = parameters;
  }

  public Map<String, Object> getParameterMap() {
    Map<String, Object> map = new HashMap<>();
    for(CypherQueryParameter p : parameters.keySet()) {
      map.put(p.getValue(), parameters.get(p));
    }
    return map;
  }

  public String getInterpolatedParamsQuery() {
    String q = this.runnableQuery;
    if (q != null) {
      for (CypherQueryParameter parameter : parameters.keySet()) {
        Object o = parameters.get(parameter);
        String v = getVariableRepresentation(o);
        q = q.replace("{" + parameter.getValue() + "}", v);
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
    } else if (o instanceof Boolean || o instanceof Integer || o instanceof Long) {
      sb.append(String.valueOf(o));
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
