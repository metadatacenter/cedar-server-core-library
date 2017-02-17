package org.metadatacenter.server.search.elasticsearch.document;

import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.search.IndexedDocumentId;

public class IndexedDocumentNode {

  private String id;

  private String cid;

  private String name;

  private String nodeType;

  public IndexedDocumentNode(String id, String cid, String name, String nodeType) {
    this.id = id;
    this.cid = cid;
    this.name = name;
    this.nodeType = nodeType;
  }

  public String getName() {
    return name;
  }

  public CedarNodeType getNodeType() {
    return CedarNodeType.forValue(nodeType);
  }

  public IndexedDocumentId buildDocumentId() {
    return new IndexedDocumentId(id);
  }
}
