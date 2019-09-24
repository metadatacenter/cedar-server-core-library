package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.constant.LinkedData;

public class CedarGroupExtract {

  private String id;
  private String name;

  public CedarGroupExtract(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @JsonProperty(LinkedData.ID)
  public String getId() {
    return id;
  }

  @JsonProperty(LinkedData.ID)
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("schema:name")
  public String getName() {
    return name;
  }

  @JsonProperty("schema:name")
  public void setName(String name) {
    this.name = name;
  }
}
