package org.metadatacenter.server.security.model;

import org.metadatacenter.model.ResourceUri;

public interface InstanceArtifactWithIsBasedOn {

  ResourceUri getIsBasedOn();

  void setIsBasedOn(String isBasedOn);
}
