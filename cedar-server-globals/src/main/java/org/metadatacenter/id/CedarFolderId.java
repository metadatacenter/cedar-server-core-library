package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarFolderId extends CedarFilesystemResourceId {

  private CedarFolderId() {
  }

  protected CedarFolderId(String id) {
    super(id);
  }

  public static CedarFolderId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarFolderId.class);
  }

  public static CedarFolderId buildSafe(String id) {
    try {
      return createFromString(id, CedarFolderId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
