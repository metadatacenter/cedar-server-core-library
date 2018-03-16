package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.time.Instant;
import java.util.Map;

public class CypherParamBuilderUser extends AbstractCypherParamBuilder {

  public static CypherParameters createUser(String userURL, String name, String displayName, String firstName,
                                            String lastName, String email) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, userURL);
    params.put(NodeProperty.NAME, name);
    params.put(NodeProperty.FIRST_NAME, firstName);
    params.put(NodeProperty.LAST_NAME, lastName);
    params.put(NodeProperty.EMAIL, email);
    params.put(NodeProperty.CREATED_ON, nowString);
    params.put(NodeProperty.CREATED_ON_TS, nowTS);
    params.put(NodeProperty.LAST_UPDATED_ON, nowString);
    params.put(NodeProperty.LAST_UPDATED_ON_TS, nowTS);
    params.put(NodeProperty.NODE_TYPE, CedarNodeType.USER.getValue());
    return params;
  }

  public static CypherParameters matchUserId(String userURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.USER_ID, userURL);
    return params;
  }

  public static CypherParameters getUserById(String userURL) {
    return getNodeByIdentity(userURL);
  }

}
