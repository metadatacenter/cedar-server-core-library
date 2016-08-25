package org.metadatacenter.server.security.model.user;

public interface ICedarUserRepresentation {

  String getUserId();

  String getFirstName();

  String getLastName();

  String getDisplayName();

  void setDisplayName(String displayName);
}