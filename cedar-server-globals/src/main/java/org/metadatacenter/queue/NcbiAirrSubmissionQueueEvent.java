package org.metadatacenter.queue;

import org.metadatacenter.submission.NcbiAirrSubmission;

public class NcbiAirrSubmissionQueueEvent extends QueueEvent {

  private NcbiAirrSubmission submission;

  public NcbiAirrSubmissionQueueEvent() {}

  public NcbiAirrSubmissionQueueEvent(NcbiAirrSubmission submission) {
    super(submission.getId(), QueueEvent.NCBI_SUBMISSION_QUEUE_ID);
    this.submission = submission;
  }

  public NcbiAirrSubmission getSubmission() {
    return submission;
  }

  @Override
  public String toString() {
    return "NcbiAirrSubmissionQueueEvent{" +
        "submission=" + submission.toString() +
        '}';
  }
}
