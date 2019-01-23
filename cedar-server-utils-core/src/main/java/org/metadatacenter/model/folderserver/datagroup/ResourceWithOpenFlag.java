package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithOpenFlag {

  @JsonProperty(NodeProperty.Label.IS_OPEN)
  public Boolean isOpen();

  @JsonProperty(NodeProperty.Label.IS_OPEN)
  public void setOpen(Boolean isOpen);

}
