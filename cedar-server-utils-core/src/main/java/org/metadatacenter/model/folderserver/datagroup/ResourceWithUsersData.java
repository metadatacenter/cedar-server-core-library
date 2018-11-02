package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithUsersData {

  @JsonProperty(NodeProperty.Label.OWNED_BY)
  public String getOwnedBy();

  @JsonProperty(NodeProperty.Label.OWNED_BY)
  public void setOwnedBy(String ownedBy);

  @JsonProperty(NodeProperty.Label.CREATED_BY)
  public String getCreatedBy();

  @JsonProperty(NodeProperty.Label.CREATED_BY)
  public void setCreatedBy(String createdBy);
  
  @JsonProperty(NodeProperty.Label.LAST_UPDATED_BY)
  public String getLastUpdatedBy();

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_BY)
  public void setLastUpdatedBy(String lastUpdatedBy);

}
