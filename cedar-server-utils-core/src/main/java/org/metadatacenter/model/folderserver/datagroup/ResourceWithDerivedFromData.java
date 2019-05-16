package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithDerivedFromData {

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  ResourceUri getDerivedFrom();

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  void setDerivedFrom(String df);

}
