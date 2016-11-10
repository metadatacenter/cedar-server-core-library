package org.metadatacenter.config;

import java.util.HashMap;

public interface ElasticsearchConfig {

  String getCluster();

  String getIndex();

  String getType();

  String getHost();

  int getTransportPort();

  int getSize();

  int getScrollKeepAlive();

  HashMap getSettings();

  HashMap getMappings();

}