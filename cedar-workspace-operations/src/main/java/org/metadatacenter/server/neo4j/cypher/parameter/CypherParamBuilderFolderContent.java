package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CypherParamBuilderFolderContent extends AbstractCypherParamBuilder {

  public static CypherParameters getFolderContentsFilteredCountParameters(String folderURL, Collection<CedarNodeType>
      nodeTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(ParameterPlaceholder.NODE_TYPE_LIST, ntl);
    if (version != null) {
      params.put(ParameterPlaceholder.VERSION, version.getValue());
    }
    if (publicationStatus != null) {
      params.put(ParameterPlaceholder.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

  public static CypherParameters getFolderContentsFilteredLookupParameters(String folderURL, Collection<CedarNodeType>
      nodeTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                               offset, String ownerId) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(ParameterPlaceholder.NODE_TYPE_LIST, ntl);
    if (version != null) {
      params.put(ParameterPlaceholder.VERSION, version.getValue());
    }
    if (publicationStatus != null) {
      params.put(ParameterPlaceholder.PUBLICATION_STATUS, publicationStatus.getValue());
    }
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    params.put(ParameterPlaceholder.USER_ID, ownerId);
    return params;
  }

}
