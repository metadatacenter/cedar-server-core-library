package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

public class CypherParamBuilderGraph extends AbstractCypherParamBuilder {

  public static CypherParameters matchNodeId(String nodeURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.NODE_ID, nodeURL);
    return params;
  }

}
