package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarElementId extends CedarSchemaArtifactId {

  private CedarElementId() {
  }

  protected CedarElementId(String id) {
    super(id);
  }

  public static CedarElementId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarElementId.class);
  }

  public static CedarElementId buildSafe(String id) {
    try {
      return createFromString(id, CedarElementId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
