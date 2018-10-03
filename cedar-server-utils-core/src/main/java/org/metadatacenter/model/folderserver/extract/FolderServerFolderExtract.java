package org.metadatacenter.model.folderserver.extract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIdAndType;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerFolderExtract extends FolderServerNodeExtract implements NodeWithIdAndType {

  private boolean userHome;
  private boolean system;
  private boolean root;

  public FolderServerFolderExtract() {
    super(CedarNodeType.FOLDER);
  }

  public static FolderServerFolderExtract fromFolder(FolderServerFolder folder) {
    try {
      return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(folder), FolderServerFolderExtract.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

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
