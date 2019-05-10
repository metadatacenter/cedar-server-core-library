package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.AbstractCedarResourceWithDates;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithUserNamesData;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithUsersData;
import org.metadatacenter.model.folderserver.datagroup.UserNamesDataGroup;
import org.metadatacenter.model.folderserver.datagroup.UsersDataGroup;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerCategory extends AbstractCedarResourceWithDates
    implements ResourceWithUsersData, ResourceWithUserNamesData {

  protected UsersDataGroup usersData;
  protected UserNamesDataGroup userNamesData;

  public FolderServerCategory() {
    super();
    this.usersData = new UsersDataGroup();
    this.userNamesData = new UserNamesDataGroup();
    this.setType(CedarResourceType.CATEGORY);
  }

  @Override
  public String getOwnedBy() {
    return usersData.getOwnedBy();
  }

  @Override
  public void setOwnedBy(String ownedBy) {
    usersData.setOwnedBy(ownedBy);
  }

  @Override
  public String getCreatedBy() {
    return usersData.getCreatedBy();
  }

  @Override
  public void setCreatedBy(String createdBy) {
    usersData.setCreatedBy(createdBy);
  }

  @Override
  public String getLastUpdatedBy() {
    return usersData.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdatedBy(String lastUpdatedBy) {
    usersData.setLastUpdatedBy(lastUpdatedBy);
  }

  @Override
  public void setOwnedByUserName(String ownedByUserName) {
    userNamesData.setOwnedByUserName(ownedByUserName);
  }

  @Override
  public String getOwnedByUserName() {
    return userNamesData.getOwnedByUserName();
  }

  @Override
  public void setCreatedByUserName(String createdByUserName) {
    userNamesData.setCreatedByUserName(createdByUserName);
  }

  @Override
  public String getCreatedByUserName() {
    return userNamesData.getCreatedByUserName();
  }

  @Override
  public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
    userNamesData.setLastUpdatedByUserName(lastUpdatedByUserName);
  }

  @Override
  public String getLastUpdatedByUserName() {
    return userNamesData.getLastUpdatedByUserName();
  }
}
