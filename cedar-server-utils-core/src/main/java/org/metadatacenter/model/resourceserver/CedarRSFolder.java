package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarFolder;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSFolder extends CedarRSNode implements CedarFolder {

  private boolean userHome;

  public CedarRSFolder() {
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
