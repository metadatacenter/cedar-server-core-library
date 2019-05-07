package org.metadatacenter.server.neo4j.cypher.parameter;

import org.apache.commons.lang.StringUtils;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.neo4j.PathUtil;
import org.metadatacenter.server.neo4j.cypher.CypherQueryParameter;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

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

  public static CypherParameters matchId(String folderURL) {
    return getNodeByIdentity(folderURL);
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

  public static CypherParameters createFolder(LinkedDataUtil linkedDataUtil, FolderServerFolder newFolder, String
      parentId) {
    String nodeId = linkedDataUtil.buildNewLinkedDataId(CedarResourceType.FOLDER);
    newFolder.setId(nodeId);
    return createNode(newFolder, parentId);
  }

}
