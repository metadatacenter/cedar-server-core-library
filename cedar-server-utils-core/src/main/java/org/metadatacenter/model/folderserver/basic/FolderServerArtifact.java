package org.metadatacenter.model.folderserver.basic;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerArtifactCurrentUserReport;
import org.metadatacenter.model.folderserver.datagroup.DerivedFromGroup;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithDerivedFromData;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithOpenFlag;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

public abstract class FolderServerArtifact extends FileSystemResource
    implements ResourceWithOpenFlag, ResourceWithDerivedFromData {

  protected DerivedFromGroup provenanceDataGroup;
  protected Boolean isOpen;

  public FolderServerArtifact(CedarResourceType resourceType) {
    super(resourceType);
    provenanceDataGroup = new DerivedFromGroup();
  }

  public static FolderServerArtifact fromFolderServerResourceCurrentUserReport(FolderServerArtifactCurrentUserReport cur) {
    try {
      String s = JsonMapper.MAPPER.writeValueAsString(cur);
      FileSystemResource folderServerNode = JsonMapper.MAPPER.readValue(s, FileSystemResource.class);
      return folderServerNode == null ? null : folderServerNode.asArtifact();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public ResourceUri getDerivedFrom() {
    return provenanceDataGroup.getDerivedFrom();
  }

  @Override
  public void setDerivedFrom(String df) {
    provenanceDataGroup.setDerivedFrom(ResourceUri.forValue(df));
  }

  @Override
  public Boolean isOpen() {
    return isOpen;
  }

  @Override
  public void setOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }

}
