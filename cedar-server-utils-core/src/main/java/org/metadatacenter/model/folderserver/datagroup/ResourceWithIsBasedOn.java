package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithIsBasedOn {

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  ResourceUri getIsBasedOn();

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  void setIsBasedOn(String isBasedOn);

}
