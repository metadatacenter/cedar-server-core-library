package org.metadatacenter.server.security.model.user;

public class CedarUserUIInfoPanel implements CedarUserUIPreferences {

  private boolean opened;

  public CedarUserUIInfoPanel() {
  }

  public boolean isOpened() {
    return opened;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
  }
}
