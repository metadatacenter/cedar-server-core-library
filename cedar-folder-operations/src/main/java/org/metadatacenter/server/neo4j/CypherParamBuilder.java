package org.metadatacenter.server.neo4j;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
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

  public static Map<String, Object> createFolder(LinkedDataUtil linkedDataUtil, String parentId, String name, String
      displayName, String description, String createdBy) {
    return createFolder(linkedDataUtil, parentId, name, displayName, description, createdBy, null);
  }

  public static Map<String, Object> createFolder(LinkedDataUtil linkedDataUtil, String parentId, String name, String
      displayName, String description, String createdBy, Map<String, Object> extraProperties) {
    String nodeId = linkedDataUtil.buildNewLinkedDataId(CedarNodeType.FOLDER);
    return createNode(parentId, nodeId, CedarNodeType.FOLDER, name, displayName, description, createdBy,
        extraProperties);
  }

  public static Map<String, Object> createResource(String parentId, String childURL, CedarNodeType nodeType,
                                                   String name, String description, String createdBy) {
    return createResource(parentId, childURL, nodeType, name, description, createdBy, null);
  }

  public static Map<String, Object> createResource(String parentId, String childURL, CedarNodeType nodeType,
                                                   String name, String description, String createdBy,
                                                   Map<String, Object> extraProperties) {
    return createNode(parentId, childURL, nodeType, name, name, description, createdBy, extraProperties);
  }

  private static Map<String, Object> createNode(String parentId, String childId, CedarNodeType nodeType, String
      name, String displayName, String description, String createdBy, Map<String, Object> extraProperties) {

    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    Map<String, Object> params = new HashMap<>();
    params.put(PARENT_ID, parentId);
    params.put(ID, childId);
    params.put(NAME, name);
    params.put(DISPLAY_NAME, displayName);
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
      extraProperties.forEach(params::put);
    }
    return params;
  }

  public static Map<String, Object> getFolderLookupByDepthParameters(PathUtil pathUtil, String path) {
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
    params.put(LIMIT, limit);
    params.put(OFFSET, offset);
    return params;
  }

  public static Map<String, Object> getFolderContentsLookupParameters(String folderURL, Collection<CedarNodeType>
      nodeTypes, int limit, int offset, String ownerId, boolean addPermissionConditions) {
    Map<String, Object> params = new HashMap<>();
    params.put(FOLDER_ID, folderURL);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(NODE_TYPE_LIST, ntl);
    params.put(LIMIT, limit);
    params.put(OFFSET, offset);
    if (addPermissionConditions) {
      params.put(Neo4JFields.USER_ID, ownerId);
    }
    return params;
  }

  public static Map<String, Object> getFolderContentsFilteredCountParameters(String folderURL, Collection<CedarNodeType>
      nodeTypes) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, folderURL);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(NODE_TYPE_LIST, ntl);
    return params;
  }

  private static Map<String, Object> getNodeByIdentity(String nodeURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, nodeURL);
    return params;
  }

  private static Map<String, Object> getNodeByIdentityAndName(String nodeURL, String nodeName) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, nodeURL);
    params.put(NAME, nodeName);
    return params;
  }

  public static Map<String, Object> getFolderContentsCountParameters(String folderURL) {
    return getNodeByIdentity(folderURL);
  }

  public static Map<String, Object> getFolderById(String folderURL) {
    return getNodeByIdentity(folderURL);
  }

  public static Map<String, Object> getNodeById(String nodeURL) {
    return getNodeByIdentity(nodeURL);
  }

  public static Map<String, Object> getResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

  public static Map<String, Object> getUserById(String userURL) {
    return getNodeByIdentity(userURL);
  }

  public static Map<String, Object> deleteFolderById(String folderURL) {
    return getNodeByIdentity(folderURL);
  }

  public static Map<String, Object> deleteResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

  public static Map<String, Object> deleteGroupById(String groupURL) {
    return getNodeByIdentity(groupURL);
  }

  public static Map<String, Object> updateFolderById(String folderURL, Map<String, String> updateFields, String
      updatedBy) {
    return updateNodeById(folderURL, updateFields, updatedBy);
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

  public static Map<String, Object> getFolderLookupByIDParameters(PathUtil pathUtil, String id) {
    return getNodeByIdentityAndName(id, pathUtil.getRootPath());
  }

  public static Map<String, Object> getNodeLookupByIDParameters(PathUtil pathUtil, String id) {
    return getNodeByIdentityAndName(id, pathUtil.getRootPath());
  }

  public static Map<String, Object> getFolderByParentIdAndName(String parentId, String name) {
    return getNodeByIdentityAndName(parentId, name);
  }

  public static Map<String, Object> createUser(String userURL, String name, String displayName, String firstName,
                                               String lastName, String email, Map<String, Object> extraProperties) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    Map<String, Object> params = new HashMap<>();
    params.put(ID, userURL);
    params.put(NAME, name);
    params.put(DISPLAY_NAME, displayName);
    params.put(FIRST_NAME, firstName);
    params.put(LAST_NAME, lastName);
    params.put(EMAIL, email);
    params.put(CREATED_ON, nowString);
    params.put(CREATED_ON_TS, nowTS);
    params.put(LAST_UPDATED_ON, nowString);
    params.put(LAST_UPDATED_ON_TS, nowTS);
    params.put(NODE_TYPE, CedarNodeType.USER.getValue());
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach(params::put);
    }
    return params;
  }

  public static Map<String, Object> createGroup(String groupURL, String name, String displayName, String description,
                                                String ownerURL, Map<String, Object> extraProperties) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    Map<String, Object> params = new HashMap<>();
    params.put(ID, groupURL);
    params.put(NAME, name);
    params.put(DISPLAY_NAME, displayName);
    params.put(DESCRIPTION, description);
    params.put(CREATED_BY, ownerURL);
    params.put(CREATED_ON, nowString);
    params.put(CREATED_ON_TS, nowTS);
    params.put(LAST_UPDATED_BY, ownerURL);
    params.put(LAST_UPDATED_ON, nowString);
    params.put(LAST_UPDATED_ON_TS, nowTS);
    params.put(NODE_TYPE, CedarNodeType.GROUP.getValue());
    params.put(USER_ID, ownerURL);
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach(params::put);
    }
    return params;
  }

  public static Map<String, Object> getNodeByParentIdAndName(String parentId, String name) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, parentId);
    params.put(NAME, name);
    return params;
  }

  public static Map<String, Object> getGroupBySpecialValue(String specialGroupName) {
    Map<String, Object> params = new HashMap<>();
    params.put(SPECIAL_GROUP, specialGroupName);
    return params;
  }

  public static Map<String, Object> getGroupById(String groupURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(ID, groupURL);
    return params;
  }

  public static Map<String, Object> getGroupByName(String groupName) {
    Map<String, Object> params = new HashMap<>();
    params.put(NAME, groupName);
    return params;
  }

  public static Map<String, Object> addGroupToUser(String userId, String groupId) {
    Map<String, Object> params = new HashMap<>();
    params.put(USER_ID, userId);
    params.put(GROUP_ID, groupId);
    return params;
  }

  public static Map<String, Object> matchFolderAndGroup(String folderURL, String groupURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(FOLDER_ID, folderURL);
    params.put(GROUP_ID, groupURL);
    return params;
  }

  public static Map<String, Object> matchFolderAndUser(String folderURL, String userURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(FOLDER_ID, folderURL);
    params.put(USER_ID, userURL);
    return params;
  }

  public static Map<String, Object> matchResourceAndGroup(String resourceURL, String groupURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(RESOURCE_ID, resourceURL);
    params.put(GROUP_ID, groupURL);
    return params;
  }

  public static Map<String, Object> matchResourceAndUser(String resourceURL, String userURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(RESOURCE_ID, resourceURL);
    params.put(USER_ID, userURL);
    return params;
  }

  public static Map<String, Object> matchNodeId(String nodeURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(NODE_ID, nodeURL);
    return params;
  }

  public static Map<String, Object> matchResourceId(String resourceURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(RESOURCE_ID, resourceURL);
    return params;
  }

  public static Map<String, Object> matchFolderId(String folderURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(FOLDER_ID, folderURL);
    return params;
  }

  public static Map<String, Object> matchUserIdAndNodeId(String userURL, String nodeURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(USER_ID, userURL);
    params.put(NODE_ID, nodeURL);
    return params;
  }

  public static Map<String, Object> matchResourceIdAndParentFolderId(String resourceId, String parentFolderId) {
    Map<String, Object> params = new HashMap<>();
    params.put(RESOURCE_ID, resourceId);
    params.put(PARENT_FOLDER_ID, parentFolderId);
    return params;
  }

  public static Map<String, Object> matchFolderIdAndParentFolderId(String folderId, String parentFolderId) {
    Map<String, Object> params = new HashMap<>();
    params.put(FOLDER_ID, folderId);
    params.put(PARENT_FOLDER_ID, parentFolderId);
    return params;
  }

  public static Map<String, Object> matchUserId(String userURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(USER_ID, userURL);
    return params;
  }

  public static Map<String, Object> matchGroupId(String groupURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(GROUP_ID, groupURL);
    return params;
  }

  public static Map<String, Object> updateGroupById(String groupURL, Map<String, String> updateFields, String
      updatedBy) {
    return updateNodeById(groupURL, updateFields, updatedBy);
  }

  public static Map<String, Object> matchFromNodeToNode(String fromURL, String toURL) {
    Map<String, Object> params = new HashMap<>();
    params.put(FROM_ID, fromURL);
    params.put(TO_ID, toURL);
    return params;
  }

  public static Map<String, Object> getSharedWithMeLookupParameters(List<CedarNodeType> nodeTypes, int limit, int
      offset, String ownerId) {
    Map<String, Object> params = new HashMap<>();
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(NODE_TYPE_LIST, ntl);
    params.put(LIMIT, limit);
    params.put(OFFSET, offset);
    params.put(Neo4JFields.USER_ID, ownerId);
    return params;
  }

  public static Map<String, Object> getSharedWithMeCountParameters(List<CedarNodeType> nodeTypes, String ownerId) {
    Map<String, Object> params = new HashMap<>();
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(NODE_TYPE_LIST, ntl);
    params.put(Neo4JFields.USER_ID, ownerId);
    return params;
  }

  public static Map<String, Object> getAllLookupParameters(List<CedarNodeType> nodeTypes, int limit, int
      offset, String ownerId, boolean addPermissionConditions) {
    Map<String, Object> params = new HashMap<>();
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(NODE_TYPE_LIST, ntl);
    params.put(LIMIT, limit);
    params.put(OFFSET, offset);
    if (addPermissionConditions) {
      params.put(Neo4JFields.USER_ID, ownerId);
    }
    return params;
  }

  public static Map<String, Object> getAllCountParameters(List<CedarNodeType> nodeTypes, String ownerId,
                                                          boolean addPermissionConditions) {
    Map<String, Object> params = new HashMap<>();
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(NODE_TYPE_LIST, ntl);
    if (addPermissionConditions) {
      params.put(Neo4JFields.USER_ID, ownerId);
    }
    return params;
  }

}
