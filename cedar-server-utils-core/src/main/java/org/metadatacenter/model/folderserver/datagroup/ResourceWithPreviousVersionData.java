package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarSchemaArtifactId;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithPreviousVersionData {

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  CedarSchemaArtifactId getPreviousVersion();

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  void setPreviousVersion(CedarSchemaArtifactId pv);

}
