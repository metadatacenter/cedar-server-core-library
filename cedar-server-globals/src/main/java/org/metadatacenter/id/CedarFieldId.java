package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarFieldId extends CedarSchemaArtifactId {

  private CedarFieldId() {
  }

  protected CedarFieldId(String id) {
    super(id);
  }

  public static CedarFieldId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarFieldId.class);
  }

  public static CedarFieldId buildSafe(String id) {
    try {
      return createFromString(id, CedarFieldId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
