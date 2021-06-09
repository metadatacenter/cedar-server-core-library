package org.metadatacenter.model.folderserver.datagroup;

public class FolderDataGroup {

  private boolean userHome;
  private boolean system;
  private boolean root;
  private String parentFolderId;

  public FolderDataGroup() {
  }

  public boolean isUserHome() {
    return userHome;
  }

  public void setUserHome(boolean userHome) {
    this.userHome = userHome;
  }

  public boolean isSystem() {
    return system;
  }

  public void setSystem(boolean system) {
    this.system = system;
  }

  public boolean isRoot() {
    return root;
  }

  public void setRoot(boolean root) {
    this.root = root;
  }

  public String getParentFolderId() { return parentFolderId; }

  public void setParentFolderId(String parentFolderId) { this.parentFolderId = parentFolderId; }
}
