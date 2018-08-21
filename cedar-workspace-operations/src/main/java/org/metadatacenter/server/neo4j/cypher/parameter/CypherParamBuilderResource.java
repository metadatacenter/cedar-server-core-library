package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.util.Map;

public class CypherParamBuilderResource extends AbstractCypherParamBuilder {

  public static CypherParameters createResource(FolderServerResource newResource, String parentId) {
    return createNode(newResource, parentId);
  }

  public static CypherParameters updateResourceById(String resourceURL, Map<NodeProperty, String> updateFields, String
      updatedBy) {
    return updateNodeById(resourceURL, updateFields, updatedBy);
  }

  public static CypherParameters deleteResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

  public static CypherParameters matchResourceId(String resourceURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.RESOURCE_ID, resourceURL);
    return params;
  }

  public static CypherParameters matchResourceIdAndUserId(String resourceURL, String userId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.RESOURCE_ID, resourceURL);
    params.put(ParameterPlaceholder.USER_ID, userId);
    return params;
  }

  public static CypherParameters getResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

}
