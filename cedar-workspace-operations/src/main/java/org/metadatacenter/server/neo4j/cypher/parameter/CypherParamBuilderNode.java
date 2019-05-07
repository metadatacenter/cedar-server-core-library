package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.neo4j.PathUtil;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.List;

public class CypherParamBuilderNode extends AbstractCypherParamBuilder {

  public static CypherParameters matchNodeId(String nodeURL) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.NODE_ID, nodeURL);
    return params;
  }

  public static CypherParameters getNodeLookupByIDParameters(PathUtil pathUtil, String id) {
    return getNodeByIdentityAndName(id, pathUtil.getRootPath());
  }

  public static CypherParameters getAllNodesLookupParameters(int limit, int offset) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    return params;
  }

  public static CypherParameters getNodeByParentIdAndName(String parentId, String name) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.ID, parentId);
    params.put(NodeProperty.NAME, name);
    return params;
  }

  public static CypherParameters getSharedWithMeLookupParameters(List<CedarResourceType> resourceTypes,
                                                                 ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, int limit, int offset, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

  public static CypherParameters getSharedWithEverybodyLookupParameters(List<CedarResourceType> resourceTypes,
                                                                        ResourceVersionFilter version,
                                                                        ResourcePublicationStatusFilter publicationStatus, int limit, int offset, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

  public static CypherParameters getSharedWithMeCountParameters(List<CedarResourceType> resourceTypes,
                                                                ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

  public static CypherParameters getSharedWithEverybodyCountParameters(List<CedarResourceType> resourceTypes,
                                                                       ResourceVersionFilter version,
                                                                       ResourcePublicationStatusFilter publicationStatus, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

  public static CypherParameters getAllLookupParameters(List<CedarResourceType> resourceTypes,
                                                        ResourceVersionFilter version,
                                                        ResourcePublicationStatusFilter publicationStatus, int limit,
                                                        int offset, String ownerId, boolean addPermissionConditions) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    if (addPermissionConditions) {
      params.put(ParameterPlaceholder.USER_ID, ownerId);
    }
    return params;
  }

  public static CypherParameters getAllCountParameters(List<CedarResourceType> resourceTypes,
                                                       ResourceVersionFilter version,
                                                       ResourcePublicationStatusFilter publicationStatus, String
                                                           ownerId, boolean addPermissionConditions) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    if (addPermissionConditions) {
      params.put(ParameterPlaceholder.USER_ID, ownerId);
    }
    return params;
  }

  public static CypherParameters getNodeById(String nodeURL) {
    return getNodeByIdentity(nodeURL);
  }

  public static CypherParameters getSearchIsBasedOnLookupParameters(List<CedarResourceType> resourceTypes,
                                                                    String isBasedOn,
                                                                    int limit, int offset, String ownerId,
                                                                    boolean addPermissionConditions) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    params.put(ParameterPlaceholder.IS_BASED_ON, isBasedOn);
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    if (addPermissionConditions) {
      params.put(ParameterPlaceholder.USER_ID, ownerId);
    }
    return params;
  }

  public static CypherParameters getSearchIsBasedOnCountParameters(List<CedarResourceType> resourceTypes,
                                                                   String isBasedOn,
                                                                   String ownerId, boolean addPermissionConditions) {
    CypherParameters params = new CypherParameters();
    params.addResourceTypes(resourceTypes);
    params.put(ParameterPlaceholder.IS_BASED_ON, isBasedOn);
    if (addPermissionConditions) {
      params.put(ParameterPlaceholder.USER_ID, ownerId);
    }
    return params;
  }

  public static CypherParameters matchNodeIdAndEverybodyPermission(String nodeId,
                                                                   NodeSharePermission everybodyPermission) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.NODE_ID, nodeId);
    params.put(ParameterPlaceholder.EVERYBODY_PERMISSION, everybodyPermission.getValue());
    return params;
  }
}
