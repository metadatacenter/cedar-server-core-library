package org.metadatacenter.server.cache.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.metadatacenter.config.CacheServerPersistent;
import org.metadatacenter.server.search.SearchPermissionQueueEvent;
import org.metadatacenter.util.json.JsonMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class CacheService {

  public final static String SEARCH_PERMISSION_QUEUE_ID = "searchPermission";
  private final CacheServerPersistent cacheConfig;
  private JedisPool pool;

  public CacheService(CacheServerPersistent cacheConfig) {
    this.cacheConfig = cacheConfig;
    pool = new JedisPool(new JedisPoolConfig(), cacheConfig.getConnection().getHost(),
        cacheConfig.getConnection().getPort(), cacheConfig.getConnection().getTimeout());
  }

  public void enqueueEvent(SearchPermissionQueueEvent event) {
    Jedis jedis = pool.getResource();
    String json = null;
    try {
      json = JsonMapper.MAPPER.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      //TODO: log this
      e.printStackTrace();
    }
    jedis.rpush(cacheConfig.getQueueName(SEARCH_PERMISSION_QUEUE_ID), json);
    jedis.close();
  }

  public Jedis getJedis() {
    return pool.getResource();
  }
}
