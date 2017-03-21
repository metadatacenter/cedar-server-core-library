package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CypherParamBuilderFolderContent extends AbstractCypherParamBuilder {

  public static CypherParameters getFolderContentsLookupParameters(String folderURL, Collection<CedarNodeType>
      nodeTypes, int limit, int offset, String ownerId, boolean addPermissionConditions) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.FOLDER_ID, folderURL);
    List<String> ntl = new ArrayList<>();
    nodeTypes.forEach(cnt -> ntl.add(cnt.getValue()));
    params.put(ParameterPlaceholder.NODE_TYPE_LIST, ntl);
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    if (addPermissionConditions) {
      params.put(ParameterPlaceholder.USER_ID, ownerId);
    }
    return params;
  }

}
