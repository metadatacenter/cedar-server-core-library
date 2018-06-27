package org.metadatacenter.model.folderserverreport;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserverextract.FolderServerNodeExtract;
import org.metadatacenter.model.folderserverextract.FolderServerResourceExtract;
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

  private FolderServerResourceExtract derivedFrom_;
  private List<FolderServerNodeExtract> versionHistory;

  public FolderServerResourceReport(CedarNodeType nodeType) {
    super(nodeType);
    this.versionHistory = new ArrayList<>();
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

  @JsonGetter("derivedFrom_")
  public FolderServerResourceExtract getDerivedFrom_() {
    return derivedFrom_;
  }

  @JsonSetter("derivedFrom_")
  public void setDerivedFrom_(FolderServerResourceExtract derivedFrom_) {
    this.derivedFrom_ = derivedFrom_;
  }

  @JsonGetter("versionHistory")
  public List<FolderServerNodeExtract> getVersionHistory() {
    return versionHistory;
  }

  @JsonSetter("versionHistory")
  public void setVersionHistory(List<FolderServerNodeExtract> versionHistory) {
    this.versionHistory = versionHistory;
  }
}
