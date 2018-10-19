package org.metadatacenter.server.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.metadatacenter.config.CacheServerPersistent;
import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.queue.util.QueueServiceWithBlockingQueue;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class AppLoggerQueueService extends QueueServiceWithBlockingQueue {

  private static final Logger log = LoggerFactory.getLogger(AppLoggerQueueService.class);

  public AppLoggerQueueService(CacheServerPersistent cacheConfig) {
    super(cacheConfig, APP_LOG_QUEUE_ID);
  }

  public void enqueueEvent(AppLogMessage message) {
    try (Jedis jedis = pool.getResource()) {
      String json = null;
      try {
        json = JsonMapper.MAPPER.writeValueAsString(message);
      } catch (JsonProcessingException e) {
        log.error("Error while enqueueing log message", e);
      }
      jedis.rpush(queueName, json);
    }
  }

}
