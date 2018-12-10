package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithPublicFlag {

  @JsonProperty(NodeProperty.Label.IS_PUBLIC)
  public Boolean isPublic();

  @JsonProperty(NodeProperty.Label.IS_PUBLIC)
  public void setPublic(Boolean isPublic);

}
