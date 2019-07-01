package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarCategoryId extends CedarResourceId {

  private CedarCategoryId() {
  }

  public static CedarCategoryId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarCategoryId.class);
  }

}
