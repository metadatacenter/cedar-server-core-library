package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public abstract class CedarInstanceArtifactId extends CedarArtifactId {

  protected CedarInstanceArtifactId() {
  }

  protected CedarInstanceArtifactId(String id) {
    super(id);
  }

  public static CedarInstanceArtifactId buildSafe(String id) {
    try {
      return createFromString(id, CedarInstanceArtifactId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
