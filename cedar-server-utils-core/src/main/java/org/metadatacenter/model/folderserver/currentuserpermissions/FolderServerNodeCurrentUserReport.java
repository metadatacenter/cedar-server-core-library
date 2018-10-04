package org.metadatacenter.model.folderserver.currentuserpermissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.NodeWithCurrentUserPermissions;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "nodeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FolderServerFolderCurrentUserReport.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = FolderServerFieldCurrentUserReport.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = FolderServerElementCurrentUserReport.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = FolderServerTemplateCurrentUserReport.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = FolderServerInstanceCurrentUserReport.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class FolderServerNodeCurrentUserReport extends FolderServerNode
    implements NodeWithCurrentUserPermissions {

  private CurrentUserPermissions currentUserPermissions = new CurrentUserPermissions();

  public FolderServerNodeCurrentUserReport(CedarNodeType nodeType) {
    super(nodeType);
  }

  public static FolderServerNodeCurrentUserReport fromNode(FolderServerNode node) {
    try {
      String s = JsonMapper.MAPPER.writeValueAsString(node);
      return JsonMapper.MAPPER.readValue(s, FolderServerNodeCurrentUserReport.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @JsonProperty(NodeProperty.OnTheFly.CURRENT_USER_PERMISSIONS)
  public CurrentUserPermissions getCurrentUserPermissions() {
    return currentUserPermissions;
  }

}
