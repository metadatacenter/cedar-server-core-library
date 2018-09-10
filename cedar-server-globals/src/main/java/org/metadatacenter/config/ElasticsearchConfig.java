package org.metadatacenter.config;

public class ElasticsearchConfig {

  private String clusterName;

  private String indexName;

  private String host;

  private int transportPort;

  private int size;

  private int scrollKeepAlive;

  public String getClusterName() {
    return clusterName;
  }

  public String getIndexName() {
    return indexName;
  }

  public String getHost() {
    return host;
  }

  public int getTransportPort() {
    return transportPort;
  }

  public int getSize() {
    return size;
  }

  public int getScrollKeepAlive() {
    return scrollKeepAlive;
  }
}