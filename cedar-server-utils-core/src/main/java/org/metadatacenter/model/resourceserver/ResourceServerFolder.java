package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceServerFolder extends ResourceServerNode {

  private boolean userHome;
  private boolean system;
  private boolean root;

  public ResourceServerFolder() {
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
}