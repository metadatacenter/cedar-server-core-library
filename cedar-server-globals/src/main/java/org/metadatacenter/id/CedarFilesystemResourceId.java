package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public abstract class CedarFilesystemResourceId extends CedarResourceId {

  protected CedarFilesystemResourceId() {
  }

  protected CedarFilesystemResourceId(String id) {
    super(id);
  }

  public static CedarFilesystemResourceId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarFilesystemResourceId.class);
  }

  public static CedarFilesystemResourceId buildSafe(String id) {
    try {
      return createFromString(id, CedarFilesystemResourceId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
