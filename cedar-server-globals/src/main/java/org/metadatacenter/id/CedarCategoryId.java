package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarCategoryId extends CedarResourceId {

  public final static String CATEGORY_ID_ROOT = "root";

  private CedarCategoryId() {
  }

  protected CedarCategoryId(String id) {
    super(id);
  }

  public static CedarCategoryId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarCategoryId.class);
  }

  public static CedarCategoryId buildSafe(String id) {
    try {
      return createFromString(id, CedarCategoryId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
