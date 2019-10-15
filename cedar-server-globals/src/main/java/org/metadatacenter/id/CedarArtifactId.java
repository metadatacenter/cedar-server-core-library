package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public abstract class CedarArtifactId extends CedarFilesystemResourceId {

  protected CedarArtifactId() {
  }

  protected CedarArtifactId(String id) {
    super(id);
  }

  public static CedarArtifactId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarArtifactId.class);
  }

  public static CedarArtifactId buildSafe(String id) {
    try {
      return createFromString(id, CedarArtifactId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
