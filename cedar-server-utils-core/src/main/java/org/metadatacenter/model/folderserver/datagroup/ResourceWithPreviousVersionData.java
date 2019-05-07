package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithPreviousVersionData {

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  ResourceUri getPreviousVersion();

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  void setPreviousVersion(String pv);

}
