package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarTemplateInstanceId extends CedarInstanceArtifactId {

  private CedarTemplateInstanceId() {
  }

  protected CedarTemplateInstanceId(String id) {
    super(id);
  }

  public static CedarTemplateInstanceId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarTemplateInstanceId.class);
  }

  public static CedarTemplateInstanceId buildSafe(String id) {
    try {
      return createFromString(id, CedarTemplateInstanceId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
