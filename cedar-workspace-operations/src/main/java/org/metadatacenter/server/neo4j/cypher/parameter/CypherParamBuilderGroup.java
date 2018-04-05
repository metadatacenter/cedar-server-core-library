package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.time.Instant;
import java.util.Map;

public class CypherParamBuilderGroup extends AbstractCypherParamBuilder {

  public static CypherParameters createGroup(String groupURL, String name, String displayName, String description,
                                             String ownerURL, String specialGroup) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, groupURL);
    params.put(NodeProperty.NAME, name);
    params.put(NodeProperty.DESCRIPTION, description);
    params.put(NodeProperty.CREATED_BY, ownerURL);
    params.put(NodeProperty.CREATED_ON, nowString);
    params.put(NodeProperty.CREATED_ON_TS, nowTS);
    params.put(NodeProperty.LAST_UPDATED_BY, ownerURL);
    params.put(NodeProperty.LAST_UPDATED_ON, nowString);
    params.put(NodeProperty.LAST_UPDATED_ON_TS, nowTS);
    params.put(NodeProperty.NODE_TYPE, CedarNodeType.GROUP.getValue());
    params.put(NodeProperty.SPECIAL_GROUP, specialGroup);
    params.put(ParameterPlaceholder.USER_ID, ownerURL);
    return params;
  }

  public static CypherParameters getGroupById(String groupURL) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, groupURL);
    return params;
  }

  public static CypherParameters getGroupByName(String groupName) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.NAME, groupName);
    return params;
  }

  public static CypherParameters updateGroupById(String groupURL, Map<NodeProperty, String> updateFields, String
      updatedBy) {
    return updateNodeById(groupURL, updateFields, updatedBy);
  }

  public static CypherParameters deleteGroupById(String groupURL) {
    return getNodeByIdentity(groupURL);
  }

  public static CypherParameters matchGroupId(String groupURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.GROUP_ID, groupURL);
    return params;
  }

  public static CypherParameters getGroupBySpecialValue(String specialGroupName) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.SPECIAL_GROUP, specialGroupName);
    return params;
  }


}
