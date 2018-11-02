package org.metadatacenter.server.valuerecommender.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValuerecommenderReindexMessage {

  private String templateId;
  private String instanceId;
  private ValuerecommenderReindexMessageResourceType resourceType;
  private ValuerecommenderReindexMessageActionType actionType;
  private Instant creationTime;

  public ValuerecommenderReindexMessage() {
  }

  public ValuerecommenderReindexMessage(String templateId, String instanceId,
                                        ValuerecommenderReindexMessageResourceType resourceType,
                                        ValuerecommenderReindexMessageActionType actionType) {
    this.templateId = templateId;
    this.instanceId = instanceId;
    this.resourceType = resourceType;
    this.actionType = actionType;
    this.creationTime = Instant.now();
  }

  public String getTemplateId() {
    return templateId;
  }

  public String getInstanceId() {
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
