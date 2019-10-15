package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarArtifactId;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithDerivedFromData {

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  CedarArtifactId getDerivedFrom();

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  void setDerivedFrom(CedarArtifactId df);

}
