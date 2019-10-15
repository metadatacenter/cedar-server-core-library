package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarTemplateId extends CedarSchemaArtifactId {

  private CedarTemplateId() {
  }

  protected CedarTemplateId(String id) {
    super(id);
  }

  public static CedarTemplateId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarTemplateId.class);
  }

  public static CedarTemplateId buildSafe(String id) {
    try {
      return createFromString(id, CedarTemplateId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
