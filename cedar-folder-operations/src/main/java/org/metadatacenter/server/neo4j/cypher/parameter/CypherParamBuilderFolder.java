package org.metadatacenter.server.neo4j.cypher.parameter;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.neo4j.PathUtil;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.CypherQueryParameter;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CypherParamBuilderFolder extends AbstractCypherParamBuilder {

  public static CypherParameters matchFolderIdAndParentFolderId(String folderId, String parentFolderId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderId);
    params.put(ParameterPlaceholder.PARENT_FOLDER_ID, parentFolderId);
    return params;
  }

  public static CypherParameters matchFolderId(String folderURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    return params;
  }

  public static CypherParameters updateFolderById(String folderURL, Map<? extends CypherQueryParameter, String>
      updateFields, String updatedBy) {
    return updateNodeById(folderURL, updateFields, updatedBy);
  }

  public static CypherParameters deleteFolderById(String folderURL) {
    return getNodeByIdentity(folderURL);
  }

  public static CypherParameters getFolderLookupByDepthParameters(PathUtil pathUtil, String path) {
    String normalizedPath = pathUtil.normalizePath(path);
    String[] parts = StringUtils.split(normalizedPath, pathUtil.getSeparator());
    CypherParameters folderNames = new CypherParameters();
    folderNames.put(getFolderAlias(0), pathUtil.getRootPath());
    for (int i = 0; i < parts.length; i++) {
      folderNames.put(getFolderAlias(i + 1), parts[i]);
    }
    return folderNames;
  }

  public static CypherParameters getFolderLookupByIDParameters(PathUtil pathUtil, String id) {
    return getNodeByIdentityAndName(id, pathUtil.getRootPath());
  }

  public static CypherParameters createFolder(LinkedDataUtil linkedDataUtil, String parentId, String name, String
      displayName, String description, String createdBy, Map<? extends CypherQueryParameter, Object> extraProperties) {
    String nodeId = linkedDataUtil.buildNewLinkedDataId(CedarNodeType.FOLDER);
    return createNode(parentId, nodeId, CedarNodeType.FOLDER, name, displayName, description, createdBy,
        extraProperties);
  }

  public static CypherParameters createFolder(LinkedDataUtil linkedDataUtil, String parentId, String name, String
      displayName, String description, String createdBy) {
    return createFolder(linkedDataUtil, parentId, name, displayName, description, createdBy, null);
  }

  public static CypherParameters getFolderById(String folderURL) {
    return getNodeByIdentity(folderURL);
  }

  public static CypherParameters getFolderContentsFilteredCountParameters(String folderURL, Collection<CedarNodeType>
      nodeTypes) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, folderURL);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(ParameterPlaceholder.NODE_TYPE_LIST, ntl);
    return params;
  }

  public static CypherParameters getFolderContentsCountParameters(String folderURL) {
    return getNodeByIdentity(folderURL);
  }

}
