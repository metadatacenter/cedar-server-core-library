package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarGroupId extends CedarResourceId {

  private CedarGroupId() {
  }

  protected CedarGroupId(String id) {
    super(id);
  }

  public static CedarGroupId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarGroupId.class);
  }

  public static CedarGroupId buildSafe(String id) {
    try {
      return createFromString(id, CedarGroupId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}