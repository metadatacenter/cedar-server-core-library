package org.metadatacenter.server.security.model.user;

import javax.validation.constraints.NotNull;

public class CedarUserUIInfoPanel {

  @NotNull
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
