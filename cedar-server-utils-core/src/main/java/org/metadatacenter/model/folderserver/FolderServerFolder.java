package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerFolder extends FolderServerNode {

  private boolean userHome;
  private boolean system;
  private boolean root;
  private String homeOf;

  public FolderServerFolder() {
    super(CedarNodeType.FOLDER);
  }

  @JsonProperty("isUserHome")
  public boolean isUserHome() {
    return userHome;
  }

  @JsonProperty("isUserHome")
  public void setUserHome(boolean userHome) {
    this.userHome = userHome;
  }

  @JsonProperty("isSystem")
  public boolean isSystem() {
    return system;
  }

  @JsonProperty("isSystem")
  public void setSystem(boolean system) {
    this.system = system;
  }

  @JsonProperty("isRoot")
  public boolean isRoot() {
    return root;
  }

  @JsonProperty("isRoot")
  public void setRoot(boolean root) {
    this.root = root;
  }

  @JsonProperty("homeOf")
  public String getHomeOf() {
    return homeOf;
  }

  @JsonProperty("homeOf")
  public void setHomeOf(String homeOf) {
    this.homeOf = homeOf;
  }
}
