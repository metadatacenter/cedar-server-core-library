package org.metadatacenter.server.neo4j;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.time.Instant;
import java.util.*;

import static org.metadatacenter.server.neo4j.Neo4JFields.*;

public class CypherParamBuilder {

  private CypherParamBuilder() {
  }

  private static String getFolderAlias(int i) {
    StringBuilder sb = new StringBuilder();
    sb.append(FOLDER_ALIAS_PREFIX);
    sb.append(i);
    return sb.toString();
  }


  public static Map<String, Object> createFolder(String parentId, String name, String description, String createdBy) {
    return createFolder(parentId, name, description, createdBy, null);
  }

  public static Map<String, Object> createFolder(String parentId, String name, String description, String createdBy,
                                                 Map<NodeExtraParameter, Object> extraProperties) {
    String nodeId = UUID.randomUUID().toString();
    return createNode(parentId, nodeId, CedarNodeType.FOLDER, name, description, createdBy, extraProperties);
  }

  public static Map<String, Object> createResource(String parentId, String childURL, CedarNodeType nodeType,
                                                   String name, String description, String createdBy) {
    return createResource(parentId, childURL, nodeType, name, description, createdBy, null);
  }

  public static Map<String, Object> createResource(String parentId, String childURL, CedarNodeType nodeType,
                                                   String name, String description, String createdBy,
                                                   Map<NodeExtraParameter, Object> extraProperties) {
    return createNode(parentId, childURL, nodeType, name, description, createdBy, extraProperties);
  }

  private static Map<String, Object> createNode(String parentId, String childId, CedarNodeType nodeType, String
      name, String description, String createdBy, Map<NodeExtraParameter, Object> extraProperties) {

    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    Map<String, Object> params = new HashMap<>();
    params.put(PARENT_ID, parentId);
    params.put(ID, childId);
    params.put(NAME, name);
    params.put(DESCRIPTION, description);
    params.put(CREATED_BY, createdBy);
    params.put(CREATED_ON, nowString);
    params.put(CREATED_ON_TS, nowTS);
    params.put(LAST_UPDATED_BY, createdBy);
    params.put(LAST_UPDATED_ON, nowString);
    params.put(LAST_UPDATED_ON_TS, nowTS);
    params.put(OWNED_BY, createdBy);
    params.put(USER_ID, createdBy);
    params.put(NODE_TYPE, nodeType.getValue());
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach((key, value) -> params.put(key.getValue(), value));
    }
    return params;
  }

  public static Map<String, Object> getFolderLookupByDepthParameters(IPathUtil pathUtil, String path) {
    String normalizedPath = pathUtil.normalizePath(path);
    String[] parts = StringUtils.split(normalizedPath, pathUtil.getSeparator());
    Map<String, Object> folderNames = new HashMap<>();
    folderNames.put(getFolderAlias(0), pathUtil.getRootPath());
    for (int i = 0; i < parts.length; i++) {
      folderNames.put(getFolderAlias(i + 1), parts[i]);
    }
    return folderNames;
  }

  public static Map<String, Object> getAllNodesLookupParameters(int limit, int offset) {
    Map<String, Object> params = new HashMap<>();
    params.put("limit", limit);
    params.put("offset", offset);
    return params;
  }

  public static Map<String, Object> getFolderContentsLookupParameters(String folderId, Collection<CedarNodeType>
      nodeTypes, int limit, int offset, String ownerId, boolean addPermissionConditions) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, folderId);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put("nodeTypeList", ntl);
    params.put("limit", limit);
    params.put("offset", offset);
    if (addPermissionConditions) {
      params.put(NodeExtraParameter.Keys.IS_PUBLICLY_READABLE, true);
      params.put(NodeExtraParameter.Keys.OWNED_BY, ownerId);
    }
    return params;
  }

  public static Map<String, Object> getFolderContentsFilteredCountParameters(String folderId, Collection<CedarNodeType>
      nodeTypes) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, folderId);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put("nodeTypeList", ntl);
    return params;
  }

  private static Map<String, Object> getNodeByIdentity(String nodeId) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, nodeId);
    return params;
  }

  private static Map<String, Object> getNodeByIdentityAndName(String nodeId, String nodeName) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, nodeId);
    params.put(NAME, nodeName);
    return params;
  }

  public static Map<String, Object> getFolderContentsCountParameters(String folderId) {
    return getNodeByIdentity(folderId);
  }

  public static Map<String, Object> getFolderById(String folderId) {
    return getNodeByIdentity(folderId);
  }

  public static Map<String, Object> getResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

  public static Map<String, Object> getUserById(String userURL) {
    return getNodeByIdentity(userURL);
  }

  public static Map<String, Object> deleteFolderById(String folderId) {
    return getNodeByIdentity(folderId);
  }

  public static Map<String, Object> deleteResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

  public static Map<String, Object> updateFolderById(String folderId, Map<String, String> updateFields, String
      updatedBy) {
    return updateNodeById(folderId, updateFields, updatedBy);
  }

  public static Map<String, Object> updateResourceById(String resourceURL, Map<String, String> updateFields, String
      updatedBy) {
    return updateNodeById(resourceURL, updateFields, updatedBy);
  }

  private static Map<String, Object> updateNodeById(String nodeId, Map<String, String> updateFields, String
      updatedBy) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    Map<String, Object> params = new HashMap<>();
    params.put(LAST_UPDATED_BY, updatedBy);
    params.put(LAST_UPDATED_ON, nowString);
    params.put(LAST_UPDATED_ON_TS, nowTS);
    params.put(ID, nodeId);
    params.putAll(updateFields);
    return params;
  }

  public static Map<String, Object> getFolderLookupByIDParameters(IPathUtil pathUtil, String id) {
    return getNodeByIdentityAndName(id, pathUtil.getRootPath());
  }

  public static Map<String, Object> getNodeLookupByIDParameters(IPathUtil pathUtil, String id) {
    return getNodeByIdentityAndName(id, pathUtil.getRootPath());
  }

  public static Map<String, Object> getFolderByParentIdAndName(String parentId, String name) {
    return getNodeByIdentityAndName(parentId, name);
  }

  public static Map<String, Object> createUser(String userURL) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    Map<String, Object> params = new HashMap<>();
    params.put(ID, userURL);
    params.put(CREATED_ON, nowString);
    params.put(CREATED_ON_TS, nowTS);
    params.put(LAST_UPDATED_ON, nowString);
    params.put(LAST_UPDATED_ON_TS, nowTS);
    params.put(NODE_TYPE, CedarNodeType.USER.getValue());
    return params;
  }

  public static Map<String, Object> getNodeByParentIdAndName(String parentId, String name) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, parentId);
    params.put(NAME, name);
    return params;
  }
}
