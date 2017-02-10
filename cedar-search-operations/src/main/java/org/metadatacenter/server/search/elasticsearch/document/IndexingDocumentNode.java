package org.metadatacenter.server.search.elasticsearch.document;

public class IndexingDocumentNode {

  private String cid;

  public IndexingDocumentNode(String cid) {
    this.cid = cid;
  }

  public String getCid() {
    return cid;
  }
}
