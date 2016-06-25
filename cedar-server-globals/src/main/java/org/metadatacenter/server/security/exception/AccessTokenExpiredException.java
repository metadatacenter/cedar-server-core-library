package org.metadatacenter.server.security.exception;

public class AccessTokenExpiredException extends CedarAccessException {

  private int expiration;

  public AccessTokenExpiredException(int expiration) {
    super("Access token expired. Please refresh it.", "tokenExpired", "refreshToken");
    this.expiration = expiration;
  }

  public int getExpiration() {
    return expiration;
  }
}
