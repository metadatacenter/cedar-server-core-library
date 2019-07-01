package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

public class CedarUserId extends CedarResourceId {

  private CedarUserId() {
  }

  public static CedarUserId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarUserId.class);
  }

  public static CedarUserId buildSafe(String id) {
    try {
      return createFromString(id, CedarUserId.class);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CedarUserId that =
        (CedarUserId) o;

    return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

}
