package org.metadatacenter.error;

public enum CedarSuggestedAction {

  NONE("none"),;

  private String value;

  CedarSuggestedAction(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
