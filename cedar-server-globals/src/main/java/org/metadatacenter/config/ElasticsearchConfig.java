package org.metadatacenter.config;

public interface ElasticsearchConfig {
  String getCluster();

  String getIndex();

  String getType();

  String getHost();

  int getTransportPort();

  int getSize();

  int getScrollKeepAlive();
}