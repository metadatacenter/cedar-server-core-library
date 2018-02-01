package org.metadatacenter.config;

import org.metadatacenter.model.search.IndexedDocumentType;

import java.util.Map;

public class ElasticsearchIndex {

  private String name;

  private Map<IndexedDocumentType, String> types;

  public String getName() {
    return name;
  }

  public Map<IndexedDocumentType, String> getTypes() {
    return types;
  }

  public String getType(IndexedDocumentType documentType) {
    return types.get(documentType);
  }

}