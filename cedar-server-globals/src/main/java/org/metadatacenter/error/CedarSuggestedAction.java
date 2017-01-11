package org.metadatacenter.error;

public enum CedarSuggestedAction {

  NONE("none"),
  REQUEST_ROLE("requestRole"),
  LOGOUT("logout"),
  REFRESH_TOKEN("refreshToken");

  private String value;

  CedarSuggestedAction(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
