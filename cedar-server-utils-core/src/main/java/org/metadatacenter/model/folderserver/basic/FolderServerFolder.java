package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerFolderCurrentUserReport;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIdAndType;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerFolder extends FolderServerNode implements NodeWithIdAndType {

  private boolean userHome;
  private boolean system;
  private boolean root;
  private String homeOf;

  public FolderServerFolder() {
    super(CedarNodeType.FOLDER);
  }

  public static FolderServerFolder fromFolderServerFolderCurrentUserReport(FolderServerFolderCurrentUserReport folder) {
    try {
      String s = JsonMapper.MAPPER.writeValueAsString(folder);
      return JsonMapper.MAPPER.readValue(s, FolderServerFolder.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @JsonProperty(NodeProperty.Label.IS_USER_HOME)
  public boolean isUserHome() {
    return userHome;
  }

  @JsonProperty(NodeProperty.Label.IS_USER_HOME)
  public void setUserHome(boolean userHome) {
    this.userHome = userHome;
  }

  @JsonProperty(NodeProperty.Label.IS_SYSTEM)
  public boolean isSystem() {
    return system;
  }

  @JsonProperty(NodeProperty.Label.IS_SYSTEM)
  public void setSystem(boolean system) {
    this.system = system;
  }

  @JsonProperty(NodeProperty.Label.IS_ROOT)
  public boolean isRoot() {
    return root;
  }

  @JsonProperty(NodeProperty.Label.IS_ROOT)
  public void setRoot(boolean root) {
    this.root = root;
  }

  @JsonProperty(NodeProperty.Label.HOME_OF)
  public String getHomeOf() {
    return homeOf;
  }

  @JsonProperty(NodeProperty.Label.HOME_OF)
  public void setHomeOf(String homeOf) {
    this.homeOf = homeOf;
  }
}
