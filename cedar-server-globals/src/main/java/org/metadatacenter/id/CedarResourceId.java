package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;

import java.lang.reflect.InvocationTargetException;

public abstract class CedarResourceId {

  private String id;

  protected CedarResourceId() {
  }

  private CedarResourceId(String id) {
    this.id = id;
  }

  protected static <T extends CedarResourceId> T createFromString(String id, Class<T> type) throws CedarProcessingException {
    try {
      return type.getDeclaredConstructor().newInstance(id);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new CedarProcessingException("Error while building wrapped resource id object", e);
    }
  }

  public String getId() {
    return id;
  }

}
