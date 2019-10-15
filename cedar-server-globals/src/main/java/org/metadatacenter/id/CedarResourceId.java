package org.metadatacenter.id;

import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarResourceType;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public abstract class CedarResourceId {

  private String id;
  private static Class[] stringConstructorArguments = new Class[1];

  static {
    stringConstructorArguments[0] = String.class;
  }

  protected CedarResourceId() {
  }

  protected CedarResourceId(String id) {
    this.id = id;
  }

  protected static <T extends CedarResourceId> T createFromString(String id, Class<T> type) throws CedarProcessingException {
    try {
      return type.getDeclaredConstructor(stringConstructorArguments).newInstance(id);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new CedarProcessingException("Error while building wrapped resource id object", e);
    }
  }

  public static CedarResourceId build(String id) throws CedarProcessingException {
    return createFromString(id, CedarResourceId.class);
  }

  public static CedarResourceId build(String id, CedarResourceType resourceType) throws CedarProcessingException {
    switch (resourceType) {
      case FOLDER:
        return CedarFolderId.build(id);
      case FIELD:
        return CedarFieldId.build(id);
      case ELEMENT:
        return CedarElementId.build(id);
      case TEMPLATE:
        return CedarTemplateId.build(id);
      case INSTANCE:
        return CedarTemplateInstanceId.build(id);
      case CATEGORY:
        return CedarCategoryId.build(id);
      case GROUP:
        return CedarGroupId.build(id);
      case USER:
        return CedarUserId.build(id);
    }
    return null;
  }

  public String getId() {
    return id;
  }

  public CedarFolderId asFolderId() throws CedarProcessingException {
    return CedarFolderId.build(this.id);
  }

  public CedarArtifactId asArtifactId() throws CedarProcessingException {
    return CedarArtifactId.build(this.id);
  }

  public CedarResourceId asResourceId() throws CedarProcessingException {
    return CedarResourceId.build(this.id);
  }

  public static CedarResourceId buildSafe(String id) {
    try {
      return createFromString(id, CedarResourceId.class);
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
    if (!(o instanceof CedarResourceId)) {
      return false;
    }
    CedarResourceId that = (CedarResourceId) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
