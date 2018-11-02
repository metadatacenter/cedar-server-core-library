package org.metadatacenter.server.valuerecommender;

import org.metadatacenter.server.valuerecommender.model.ValuerecommenderReindexMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValuerecommenderReindexExecutorService {

  private static final Logger log = LoggerFactory.getLogger(ValuerecommenderReindexExecutorService.class);

  public ValuerecommenderReindexExecutorService() {
  }

  public void handleMessages(List<ValuerecommenderReindexMessage> messageList) {
    log.debug("Working on a list of valuerecommender messages...");
    Set<String> uniqueTemplateIds = new HashSet<>();
    for (ValuerecommenderReindexMessage message : messageList) {
      log.debug("Handle message: " + message);
      uniqueTemplateIds.add(message.getTemplateId());
    }
    log.debug("Working on the final template Ids...");
    for (String id : uniqueTemplateIds) {
      log.debug("Handle id: " + id);
    }
  }
}
