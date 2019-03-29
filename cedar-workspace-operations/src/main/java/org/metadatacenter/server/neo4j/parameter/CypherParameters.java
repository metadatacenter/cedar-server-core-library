package org.metadatacenter.server.neo4j.parameter;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.server.neo4j.cypher.CypherQueryParameter;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.util.Neo4JUtil;
import org.metadatacenter.server.security.model.user.CedarUserApiKey;

import java.util.*;

public class CypherParameters {

  private Map<CypherQueryParameter, Object> map;

  public CypherParameters() {
    map = new HashMap<>();
  }

  public void put(CypherQueryParameter parameter, String value) {
    map.put(parameter, value);
  }

  public void put(CypherQueryParameter parameter, long value) {
    map.put(parameter, value);
  }

  public void put(CypherQueryParameter parameter, ResourceVersion object) {
    map.put(parameter, object.getValue());
  }

  public void put(CypherQueryParameter parameter, BiboStatus object) {
    map.put(parameter, object.getValue());
  }

  public void put(CypherQueryParameter parameter, ResourceUri object) {
    map.put(parameter, object.getValue());
  }

  public void put(CypherQueryParameter parameter, Boolean value) {
    map.put(parameter, value);
  }

  public void put(CypherQueryParameter parameter, JsonNode value) {
    map.put(parameter, value);
  }

  public void put(ParameterPlaceholder parameter, List<String> value) {
    map.put(parameter, value);
  }

  public void put(NodeProperty parameter, List<String> value) {
    map.put(parameter, value);
  }

  public Map<String, Object> asMap() {
    Map<String, Object> r = new HashMap<>();
    for (CypherQueryParameter k : map.keySet()) {
      r.put(Neo4JUtil.escapePropertyName(k.getValue()), map.get(k));
    }
    return r;
  }

  public void addNodeTypes(Collection<CedarNodeType> nodeTypes) {
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    map.put(ParameterPlaceholder.NODE_TYPE_LIST, ntl);
  }
}
