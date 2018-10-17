package org.metadatacenter.config;

public class ElasticsearchConfig {

  private String clusterName;

  private ElasticsearchIndexes indexes;

  private String host;

  private int transportPort;

  private int size;

  private int scrollKeepAlive;

  public String getClusterName() {
    return clusterName;
  }

  public ElasticsearchIndexes getIndexes() {
    return indexes;
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