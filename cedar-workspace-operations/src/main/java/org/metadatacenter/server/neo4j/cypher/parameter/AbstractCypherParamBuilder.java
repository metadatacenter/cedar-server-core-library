package org.metadatacenter.server.neo4j.cypher.parameter;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.neo4j.cypher.CypherQueryParameter;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterLiteral;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.time.Instant;
import java.util.Map;

public abstract class AbstractCypherParamBuilder {

  public static final String FOLDER_ALIAS_PREFIX = "f";

  protected static ParameterLiteral getFolderAlias(int i) {
    return new ParameterLiteral(FOLDER_ALIAS_PREFIX + i);
  }

  protected static CypherParameters createNode(FolderServerNode newNode, String parentId) {

    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.PARENT_ID, parentId);
    params.put(ParameterPlaceholder.USER_ID, newNode.getOwnedBy());

    params.put(NodeProperty.ID, newNode.getId());
    params.put(NodeProperty.NAME, newNode.getName());
    params.put(NodeProperty.DESCRIPTION, newNode.getDescription());
    params.put(NodeProperty.CREATED_BY, newNode.getCreatedBy());
    params.put(NodeProperty.CREATED_ON, nowString);
    params.put(NodeProperty.CREATED_ON_TS, nowTS);
    params.put(NodeProperty.LAST_UPDATED_BY, newNode.getLastUpdatedBy());
    params.put(NodeProperty.LAST_UPDATED_ON, nowString);
    params.put(NodeProperty.LAST_UPDATED_ON_TS, nowTS);
    params.put(NodeProperty.OWNED_BY, newNode.getOwnedBy());
    params.put(NodeProperty.NODE_TYPE, newNode.getType().getValue());

    if (newNode instanceof FolderServerResource) {
      FolderServerResource newResource = (FolderServerResource) newNode;
      if (newResource.getVersion() != null) {
        params.put(NodeProperty.VERSION, newResource.getVersion().getValue());
      }
      if (newResource.getStatus() != null) {
        params.put(NodeProperty.STATUS, newResource.getStatus().getValue());
      }
      if (newResource.getDerivedFrom() != null) {
        params.put(NodeProperty.DERIVED_FROM, newResource.getDerivedFrom());
      }
      if (newResource.getPreviousVersion() != null) {
        params.put(NodeProperty.PREVIOUS_VERSION, newResource.getPreviousVersion());
      }
    } else if (newNode instanceof FolderServerFolder) {
      FolderServerFolder newFolder = (FolderServerFolder) newNode;
      params.put(NodeProperty.IS_ROOT, newFolder.isRoot());
      params.put(NodeProperty.IS_SYSTEM, newFolder.isSystem());
      params.put(NodeProperty.IS_USER_HOME, newFolder.isUserHome());
      params.put(NodeProperty.HOME_OF, newFolder.getHomeOf());
    }
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
      if (key != null) {
        CypherQueryParameter param = NodeProperty.forValue(key);
        if (param != null) {
          params.put(param, entry.getValue());
        }
      }
    }
    return params;
  }

  public static CypherParameters matchSourceAndTarget(String sourceId, String targetId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.SOURCE_ID, sourceId);
    params.put(ParameterPlaceholder.TARGET_ID, targetId);
    return params;
  }
}