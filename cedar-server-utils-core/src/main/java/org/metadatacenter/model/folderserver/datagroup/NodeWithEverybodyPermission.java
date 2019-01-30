package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;

public interface NodeWithEverybodyPermission {

  @JsonProperty(NodeProperty.Label.EVERYBODY_PERMISSION)
  public NodeSharePermission getEverybodyPermission();

  @JsonProperty(NodeProperty.Label.EVERYBODY_PERMISSION)
  public void setEverybodyPermission(NodeSharePermission permission);

}
