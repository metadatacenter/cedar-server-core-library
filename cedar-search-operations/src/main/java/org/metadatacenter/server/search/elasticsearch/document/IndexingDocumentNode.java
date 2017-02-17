package org.metadatacenter.server.search.elasticsearch.document;

import org.metadatacenter.model.CedarNodeType;

public class IndexingDocumentNode {

  private String cid;

  private String name;

  private String nodeType;

  public IndexingDocumentNode(String cid, String name, String nodeType) {
    this.cid = cid;
    this.name = name;
    this.nodeType = nodeType;
  }

  public String getCid() {
    return cid;
  }

  public String getName() {
    return name;
  }

  public CedarNodeType getNodeType() {
    return CedarNodeType.forValue(nodeType);
  }

}
