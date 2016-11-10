package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserRepresentation;

public class AuthorisedUser implements CedarUserRepresentation {

  private String id;
  private String firstName;
  private String lastName;
  private String email;

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
