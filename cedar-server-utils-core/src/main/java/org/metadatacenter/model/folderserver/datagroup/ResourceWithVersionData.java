package org.metadatacenter.model.folderserver.datagroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface ResourceWithVersionData {

  @JsonProperty(NodeProperty.Label.VERSION)
  public ResourceVersion getVersion();

  @JsonProperty(NodeProperty.Label.VERSION)
  public void setVersion(String versionString);

  @JsonProperty(NodeProperty.Label.IS_LATEST_VERSION)
  Boolean isLatestVersion();

  @JsonProperty(NodeProperty.Label.IS_LATEST_VERSION)
  public void setLatestVersion(Boolean latestVersion);

  @JsonProperty(NodeProperty.Label.IS_LATEST_DRAFT_VERSION)
  public Boolean isLatestDraftVersion();

  @JsonProperty(NodeProperty.Label.IS_LATEST_DRAFT_VERSION)
  public void setLatestDraftVersion(Boolean latestDraftVersion);

  @JsonProperty(NodeProperty.Label.IS_LATEST_PUBLISHED_VERSION)
  public Boolean isLatestPublishedVersion();

  @JsonProperty(NodeProperty.Label.IS_LATEST_PUBLISHED_VERSION)
  public void setLatestPublishedVersion(Boolean latestPublishedVersion);
}
