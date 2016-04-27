package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarFolder;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSFolder extends CedarFSNode implements CedarFolder {

  private boolean userHome;

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
}
