package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.model.*;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.util.Map;

public class CypherParamBuilderResource extends AbstractCypherParamBuilder {

  public static CypherParameters createResource(String parentId, String childURL, CedarNodeType nodeType,
                                                String name, String description, String createdBy, ResourceVersion
                                                    version, BiboStatus status) {
    return createNode(parentId, childURL, nodeType, name, description, createdBy, version, status, IsRoot.FALSE,
        IsSystem.FALSE, IsUserHome.FALSE, null);
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

  public static CypherParameters getResourceById(String resourceURL) {
    return getNodeByIdentity(resourceURL);
  }

}
