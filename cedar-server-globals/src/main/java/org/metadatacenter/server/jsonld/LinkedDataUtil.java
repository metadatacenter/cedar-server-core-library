package org.metadatacenter.server.jsonld;

import org.metadatacenter.config.LinkedDataConfig;
import org.metadatacenter.model.CedarNodeType;

public class LinkedDataUtil {

  private LinkedDataConfig ldConfig;

  public LinkedDataUtil(LinkedDataConfig ldConfig) {
    this.ldConfig = ldConfig;
  }

  public String getLinkedDataPrefix(CedarNodeType nodeType) {
    return ldConfig.getBase() + nodeType.getPrefix() + "/";
  }

  public String getLinkedDataId(CedarNodeType nodeType, String uuid) {
    return getLinkedDataPrefix(nodeType) + uuid;
  }

}
