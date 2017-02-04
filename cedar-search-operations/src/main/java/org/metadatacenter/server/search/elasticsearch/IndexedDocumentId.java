package org.metadatacenter.server.search.elasticsearch;

public class IndexedDocumentId {
  private String id;

  public IndexedDocumentId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
