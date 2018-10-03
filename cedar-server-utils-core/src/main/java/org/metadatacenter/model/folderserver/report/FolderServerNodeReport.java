package org.metadatacenter.model.folderserver.report;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerNodeCurrentUserReport;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "nodeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FolderServerFolderReport.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = FolderServerFieldReport.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = FolderServerElementReport.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = FolderServerTemplateReport.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = FolderServerInstanceReport.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class FolderServerNodeReport extends FolderServerNodeCurrentUserReport {

  public FolderServerNodeReport(CedarNodeType nodeType) {
    super(nodeType);
  }

}
