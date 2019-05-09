package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.Collection;

public class CypherParamBuilderFolderContent extends AbstractCypherParamBuilder {

  public static CypherParameters getFolderContentsFilteredCountParameters(String folderURL,
                                                                          Collection<CedarResourceType> resourceTypes
      , ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

  public static CypherParameters getFolderContentsFilteredLookupParameters(String folderURL,
                                                                           Collection<CedarResourceType>
                                                                               resourceTypes,
                                                                           ResourceVersionFilter version,
                                                                           ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                               offset, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    params.addResourceTypes(resourceTypes);
    if (publicationStatus != null) {
      params.put(NodeProperty.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

}
