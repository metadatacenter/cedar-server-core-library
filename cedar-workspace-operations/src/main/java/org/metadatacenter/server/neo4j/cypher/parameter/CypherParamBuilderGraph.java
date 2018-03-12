package org.metadatacenter.server.neo4j.cypher.parameter;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import static org.metadatacenter.model.ModelPaths.*;

public class CypherParamBuilderGraph extends AbstractCypherParamBuilder {

  public static CypherParameters matchNodeId(String nodeURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.NODE_ID, nodeURL);
    return params;
  }

  public static void tweakNodeProperties(JsonNode node, CypherParameters params) {
    params.put(NodeProperty.CREATED_BY, node.at(PAV_CREATED_BY).textValue());
    params.put(NodeProperty.CREATED_ON, node.at(PAV_CREATED_ON).textValue());
    params.put(NodeProperty.LAST_UPDATED_BY, node.at(OSLC_MODIFIED_BY).textValue());
    params.put(NodeProperty.LAST_UPDATED_ON, node.at(PAV_LAST_UPDATED_ON).textValue());
    params.put(NodeProperty.NAME, node.at(SCHEMA_NAME).textValue());
    params.put(NodeProperty.DESCRIPTION, node.at(SCHEMA_DESCRIPTION).textValue());
    params.put(NodeProperty.ID, node.at(AT_ID).textValue());
  }

  public static void tweakGroupProperties(JsonNode node, CypherParameters params) {
    params.put(NodeProperty.CREATED_BY, node.at(CREATED_BY).textValue());
    params.put(NodeProperty.LAST_UPDATED_BY, node.at(LAST_UPDATED_BY).textValue());
  }
}
