package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public abstract class CedarSchemaArtifactId extends CedarArtifactId {

  protected CedarSchemaArtifactId() {
  }

  protected CedarSchemaArtifactId(String id) {
    super(id);
  }

  public static CedarSchemaArtifactId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarSchemaArtifactId.class);
  }

  public static CedarSchemaArtifactId buildSafe(String id) {
    try {
      return createFromString(id, CedarSchemaArtifactId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
