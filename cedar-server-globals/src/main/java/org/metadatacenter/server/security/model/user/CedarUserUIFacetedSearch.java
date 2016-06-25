package org.metadatacenter.server.security.model.user;

public class CedarUserUIFacetedSearch implements CedarUserUIPreferences {

  private boolean opened;

  public CedarUserUIFacetedSearch() {
  }

  public boolean isOpened() {
    return opened;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
  }
}
