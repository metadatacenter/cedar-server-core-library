package org.metadatacenter.server.cache.submission;

import org.metadatacenter.queue.NcbiAirrSubmissionQueueEvent;
import org.metadatacenter.server.cache.util.CacheService;
import org.metadatacenter.submission.NcbiAirrSubmission;

public class NcbiAirrSubmissionEnqueueService {

  private final CacheService cacheService;

  public NcbiAirrSubmissionEnqueueService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  public void enqueueSubmission(NcbiAirrSubmission submission) {
    NcbiAirrSubmissionQueueEvent event = new NcbiAirrSubmissionQueueEvent(submission);
    cacheService.enqueueEvent(event);
  }

}

