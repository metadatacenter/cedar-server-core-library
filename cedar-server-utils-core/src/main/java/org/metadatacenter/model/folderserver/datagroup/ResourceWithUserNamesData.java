package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithUserNamesData {

  @JsonProperty(NodeProperty.OnTheFly.OWNED_BY_USER_NAME)
  public void setOwnedByUserName(String ownedByUserName);

  @JsonProperty(NodeProperty.OnTheFly.OWNED_BY_USER_NAME)
  public String getOwnedByUserName();

  @JsonProperty(NodeProperty.OnTheFly.CREATED_BY_USER_NAME)
  public void setCreatedByUserName(String createdByUserName);

  @JsonProperty(NodeProperty.OnTheFly.CREATED_BY_USER_NAME)
  public String getCreatedByUserName();

  @JsonProperty(NodeProperty.OnTheFly.LAST_UPDATED_BY_USER_NAME)
  public void setLastUpdatedByUserName(String lastUpdatedByUserName);

  @JsonProperty(NodeProperty.OnTheFly.LAST_UPDATED_BY_USER_NAME)
  public String getLastUpdatedByUserName();

}
