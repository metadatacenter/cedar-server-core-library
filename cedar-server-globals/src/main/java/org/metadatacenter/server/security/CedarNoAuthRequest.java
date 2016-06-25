package org.metadatacenter.server.security;

import org.metadatacenter.server.security.model.IAuthRequest;

public class CedarNoAuthRequest implements IAuthRequest {

  CedarNoAuthRequest() {
  }

  @Override
  public String getAuthString() {
    return null;
  }

  @Override
  public String getAuthHeader() {
    return null;
  }
}
