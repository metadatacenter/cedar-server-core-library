package org.metadatacenter.server.security.exception;

public class ApiKeyNotFoundException extends CedarAccessException {

  private String apiKey;

  public ApiKeyNotFoundException(String apiKey) {
    super("The apiKey was not found: '" + apiKey + "'", "apiKeyNotFound", null);
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }
}
