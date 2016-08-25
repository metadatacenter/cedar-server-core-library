package org.metadatacenter.server.security.model.user;

public class CedarUserExtract implements ICedarUserRepresentation {

  private String userId;
  private String firstName;
  private String lastName;
  private String displayName;

  public CedarUserExtract(String userId, String firstName, String lastName, String displayName) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.displayName = displayName;
  }

  @Override
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}