package org.metadatacenter.server.search.elasticsearch.document;

public class IndexingDocumentNode {

  private String cid;

  private String name;

  public IndexingDocumentNode(String cid, String name) {
    this.cid = cid;
    this.name = name;
  }

  public String getCid() {
    return cid;
  }

  public String getName() {
    return name;
  }
}
