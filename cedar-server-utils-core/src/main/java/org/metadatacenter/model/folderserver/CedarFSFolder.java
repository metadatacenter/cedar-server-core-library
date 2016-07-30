package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSFolder extends CedarFSNode {

  private boolean userHome;
  private boolean system;
  private boolean root;

  public CedarFSFolder() {
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

  public String getUUID() {
    String sid = getId();
    String prefix = CedarConfig.getInstance().getLinkedDataPrefix(CedarNodeType.FOLDER);
    if (sid != null && prefix != null && sid.startsWith(prefix)) {
      return sid.substring(prefix.length());
    } else {
      return sid;
    }
  }
}
