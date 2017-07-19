package org.metadatacenter.queue;

import org.metadatacenter.constant.CedarConstants;

import java.time.Instant;

public abstract class QueueEvent {

  public final static String SEARCH_PERMISSION_QUEUE_ID = "searchPermission";
  public final static String NCBI_SUBMISSION_QUEUE_ID = "ncbiAirrSubmission";

  private String id;
  private String queueId;
  private String createdAt;
  private long createdAtTS;

  public QueueEvent() {
  }

  public QueueEvent(String id, String queueId) {
    this.id = id;
    this.queueId = queueId;
    Instant now = Instant.now();
    this.createdAt = CedarConstants.xsdDateTimeFormatter.format(now);
    this.createdAtTS = now.getEpochSecond();
  }

  public String getId() {
    return id;
  }

  public String getQueueId() { return queueId; }

  public String getCreatedAt() {
    return createdAt;
  }

  public long getCreatedAtTS() {
    return createdAtTS;
  }

}
