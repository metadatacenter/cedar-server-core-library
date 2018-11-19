package org.metadatacenter.model.folderserver.currentuserpermissions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.FolderWithCurrentUserPermissions;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerFolderCurrentUserReport extends FolderServerNodeCurrentUserReport
    implements FolderWithCurrentUserPermissions {

  public FolderServerFolderCurrentUserReport() {
    super(CedarNodeType.FOLDER);
  }

  private boolean userHome;
  private boolean system;
  private boolean root;

  @JsonIgnore
  public boolean isUserHome() {
    return userHome;
  }

  @JsonProperty(NodeProperty.Label.IS_USER_HOME)
  public void setUserHome(boolean userHome) {
    this.userHome = userHome;
  }

  @JsonIgnore
  public boolean isSystem() {
    return system;
  }

  @JsonProperty(NodeProperty.Label.IS_SYSTEM)
  public void setSystem(boolean system) {
    this.system = system;
  }

  @JsonIgnore
  public boolean isRoot() {
    return root;
  }

  @JsonProperty(NodeProperty.Label.IS_ROOT)
  public void setRoot(boolean root) {
    this.root = root;
  }
}
