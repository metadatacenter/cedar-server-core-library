package org.metadatacenter.server.security.model.user;

import javax.validation.constraints.NotNull;

public class CedarUserUIResourceTypeFilters {

  @NotNull
  private boolean field;

  @NotNull
  private boolean element;

  @NotNull
  private boolean template;

  @NotNull
  private boolean instance;

  public CedarUserUIResourceTypeFilters() {
  }

  public boolean isField() {
    return field;
  }

  public void setField(boolean field) {
    this.field = field;
  }

  public boolean isElement() {
    return element;
  }

  public void setElement(boolean element) {
    this.element = element;
  }

  public boolean isTemplate() {
    return template;
  }

  public void setTemplate(boolean template) {
    this.template = template;
  }

  public boolean isInstance() {
    return instance;
  }

  public void setInstance(boolean instance) {
    this.instance = instance;
  }
}
