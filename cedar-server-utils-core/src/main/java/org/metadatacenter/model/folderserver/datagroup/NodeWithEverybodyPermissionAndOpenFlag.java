package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;

public interface NodeWithEverybodyPermissionAndOpenFlag {

  @JsonProperty(NodeProperty.Label.EVERYBODY_PERMISSION)
  public NodeSharePermission getEverybodyPermission();

  @JsonProperty(NodeProperty.Label.EVERYBODY_PERMISSION)
  public void setEverybodyPermission(NodeSharePermission permission);

  @JsonProperty(NodeProperty.Label.IS_OPEN)
  public Boolean isOpen();

  @JsonProperty(NodeProperty.Label.IS_OPEN)
  public void setOpen(Boolean isOpen);

}
