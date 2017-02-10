package org.metadatacenter.config;

import org.metadatacenter.model.search.IndexedDocumentType;

import java.util.Map;

public class ElasticsearchConfig {

  private String clusterName;

  private String indexName;

  private Map<IndexedDocumentType, String> types;

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

  public Map<IndexedDocumentType, String> getTypes() {
    return types;
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

  public String getType(IndexedDocumentType documentType) {
    return types.get(documentType);
  }
}