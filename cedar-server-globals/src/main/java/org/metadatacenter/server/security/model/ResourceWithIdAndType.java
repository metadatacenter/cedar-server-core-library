package org.metadatacenter.server.security.model;

import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.model.CedarResourceType;

public interface ResourceWithIdAndType {

  String getId();

  CedarResourceType getType();

  CedarFilesystemResourceId getResourceId();

}
