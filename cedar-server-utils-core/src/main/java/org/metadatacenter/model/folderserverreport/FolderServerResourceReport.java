package org.metadatacenter.model.folderserverreport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserverextract.FolderServerNodeExtract;
import org.metadatacenter.model.folderserverextract.FolderServerResourceExtract;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "nodeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FolderServerFieldReport.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = FolderServerElementReport.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = FolderServerTemplateReport.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = FolderServerInstanceReport.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class FolderServerResourceReport extends FolderServerResource {

  private FolderServerResourceExtract derivedFromExtract;
  private List<FolderServerNodeExtract> versions;

  public FolderServerResourceReport(CedarNodeType nodeType) {
    super(nodeType);
    this.versions = new ArrayList<>();
  }

  public static FolderServerResourceReport fromResource(FolderServerResource resource) {
    try {
      String s = JsonMapper.MAPPER.writeValueAsString(resource);
      return JsonMapper.MAPPER.readValue(s, FolderServerResourceReport.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @JsonProperty(NodeProperty.OnTheFly.DERIVED_FROM)
  public FolderServerResourceExtract getDerivedFromExtract() {
    return derivedFromExtract;
  }

  @JsonProperty(NodeProperty.OnTheFly.DERIVED_FROM)
  public void setDerivedFromExtract(FolderServerResourceExtract derivedFromExtract) {
    this.derivedFromExtract = derivedFromExtract;
  }

  @JsonProperty(NodeProperty.OnTheFly.VERSIONS)
  public List<FolderServerNodeExtract> getVersions() {
    return versions;
  }

  @JsonProperty(NodeProperty.OnTheFly.VERSIONS)
  public void setVersions(List<FolderServerNodeExtract> versions) {
    this.versions = versions;
  }
}