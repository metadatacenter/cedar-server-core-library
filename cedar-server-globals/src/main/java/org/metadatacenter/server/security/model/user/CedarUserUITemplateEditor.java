package org.metadatacenter.server.security.model.user;

import javax.validation.constraints.NotNull;

public class CedarUserUITemplateEditor {

  @NotNull
  private boolean templateJsonViewer;

  public CedarUserUITemplateEditor() {
  }

  public boolean isTemplateJsonViewer() {
    return templateJsonViewer;
  }

  public void setTemplateJsonViewer(boolean templateJsonViewer) {
    this.templateJsonViewer = templateJsonViewer;
  }
}
