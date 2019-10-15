package org.metadatacenter.server.valuerecommender.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.id.CedarInstanceArtifactId;
import org.metadatacenter.id.CedarTemplateId;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValuerecommenderReindexMessage {

  private CedarTemplateId templateId;
  private CedarInstanceArtifactId instanceId;
  private ValuerecommenderReindexMessageResourceType resourceType;
  private ValuerecommenderReindexMessageActionType actionType;
  private Instant creationTime;

  public ValuerecommenderReindexMessage() {
  }

  public ValuerecommenderReindexMessage(CedarTemplateId templateId, CedarInstanceArtifactId instanceId,
                                        ValuerecommenderReindexMessageResourceType resourceType,
                                        ValuerecommenderReindexMessageActionType actionType) {
    this.templateId = templateId;
    this.instanceId = instanceId;
    this.resourceType = resourceType;
    this.actionType = actionType;
    this.creationTime = Instant.now();
  }

  public CedarTemplateId getTemplateId() {
    return templateId;
  }

  public CedarInstanceArtifactId getInstanceId() {
    return instanceId;
  }

  public ValuerecommenderReindexMessageResourceType getResourceType() {
    return resourceType;
  }

  public ValuerecommenderReindexMessageActionType getActionType() {
    return actionType;
  }

  public Instant getCreationTime() {
    return creationTime;
  }
}
