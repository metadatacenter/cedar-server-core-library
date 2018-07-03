package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NodePermissionUser {

  private String id;

  public NodePermissionUser() {
  }

  public NodePermissionUser(String id) {
    this.id = id;
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NodePermissionUser that = (NodePermissionUser) o;

    return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }
}
