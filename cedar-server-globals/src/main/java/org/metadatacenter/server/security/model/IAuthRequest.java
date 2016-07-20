package org.metadatacenter.server.security.model;

public interface IAuthRequest {

  String getAuthString();

  String getAuthHeader();
}
