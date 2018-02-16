package org.metadatacenter.server.neo4j.cypher.parameter;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.IsRoot;
import org.metadatacenter.model.IsSystem;
import org.metadatacenter.model.IsUserHome;
import org.metadatacenter.server.neo4j.parameter.*;

import java.time.Instant;
import java.util.Map;

public abstract class AbstractCypherParamBuilder {

  public static final String FOLDER_ALIAS_PREFIX = "f";

  protected static ParameterLiteral getFolderAlias(int i) {
    return new ParameterLiteral(FOLDER_ALIAS_PREFIX + i);
  }

  protected static CypherParameters createNode(String parentId, String childId, CedarNodeType nodeType, String name,
                                               String displayName, String description, String createdBy, IsRoot
                                                   isRoot, IsSystem isSystem, IsUserHome isUserHome, String homeOf) {

    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.PARENT_ID, parentId);
    params.put(ParameterPlaceholder.USER_ID, createdBy);

    params.put(NodeProperty.ID, childId);
    params.put(NodeProperty.NAME, name);
    params.put(NodeProperty.DESCRIPTION, description);
    params.put(NodeProperty.CREATED_BY, createdBy);
    params.put(NodeProperty.CREATED_ON, nowString);
    params.put(NodeProperty.CREATED_ON_TS, nowTS);
    params.put(NodeProperty.LAST_UPDATED_BY, createdBy);
    params.put(NodeProperty.LAST_UPDATED_ON, nowString);
    params.put(NodeProperty.LAST_UPDATED_ON_TS, nowTS);
    params.put(NodeProperty.OWNED_BY, createdBy);
    params.put(NodeProperty.NODE_TYPE, nodeType.getValue());
    params.put(NodeProperty.IS_ROOT, isRoot);
    params.put(NodeProperty.IS_SYSTEM, isSystem);
    params.put(NodeProperty.IS_USER_HOME, isUserHome);
    params.put(NodeProperty.HOME_OF, homeOf);

    return params;
  }

  protected static CypherParameters updateNodeById(String nodeId, Map<? extends CypherQueryParameter, String>
      updateFields, String updatedBy) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.LAST_UPDATED_BY, updatedBy);
    params.put(NodeProperty.LAST_UPDATED_ON, nowString);
    params.put(NodeProperty.LAST_UPDATED_ON_TS, nowTS);
    params.put(ParameterPlaceholder.ID, nodeId);
    for (CypherQueryParameter parameter : updateFields.keySet()) {
      params.put(parameter, updateFields.get(parameter));
    }
    return params;
  }

  protected static CypherParameters getNodeByIdentity(String nodeURL) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, nodeURL);
    return params;
  }

  protected static CypherParameters getNodeByIdentityAndName(String nodeURL, String nodeName) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, nodeURL);
    params.put(NodeProperty.NAME, nodeName);
    return params;
  }

  public static CypherParameters matchFolderAndUser(String folderURL, String userURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    params.put(ParameterPlaceholder.USER_ID, userURL);
    return params;
  }

  public static CypherParameters matchFromNodeToNode(String fromURL, String toURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FROM_ID, fromURL);
    params.put(ParameterPlaceholder.TO_ID, toURL);
    return params;
  }

  public static CypherParameters matchUserAndGroup(String userId, String groupId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.USER_ID, userId);
    params.put(ParameterPlaceholder.GROUP_ID, groupId);
    return params;
  }

  public static CypherParameters matchFolderAndGroup(String folderURL, String groupURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    params.put(ParameterPlaceholder.GROUP_ID, groupURL);
    return params;
  }

  public static CypherParameters matchResourceAndGroup(String resourceURL, String groupURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.RESOURCE_ID, resourceURL);
    params.put(ParameterPlaceholder.GROUP_ID, groupURL);
    return params;
  }

  public static CypherParameters matchResourceAndUser(String resourceURL, String userURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.RESOURCE_ID, resourceURL);
    params.put(ParameterPlaceholder.USER_ID, userURL);
    return params;
  }

  public static CypherParameters matchUserIdAndNodeId(String userURL, String nodeURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.USER_ID, userURL);
    params.put(ParameterPlaceholder.NODE_ID, nodeURL);
    return params;
  }

  public static CypherParameters matchResourceIdAndParentFolderId(String resourceId, String parentFolderId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.RESOURCE_ID, resourceId);
    params.put(ParameterPlaceholder.PARENT_FOLDER_ID, parentFolderId);
    return params;
  }

  public static CypherParameters mapAllProperties(JsonNode node) {
    CypherParameters params = new CypherParameters();
    for (Map.Entry<String, JsonNode> entry : (Iterable<Map.Entry<String, JsonNode>>) () -> node.fields()) {
      String key = entry.getKey();
      System.out.println("aa:" + key);
      CypherQueryParameter param = NodeProperty.forValue(key);
      params.put(param, entry.getValue());
    }
    return params;
  }
}