package org.metadatacenter.server.jsonld;

import org.metadatacenter.config.LinkedDataConfig;
import org.metadatacenter.model.CedarNodeType;

import java.util.UUID;

public class LinkedDataUtil {

  private final LinkedDataConfig ldConfig;

  public LinkedDataUtil(LinkedDataConfig ldConfig) {
    this.ldConfig = ldConfig;
  }

  private String getLinkedDataPrefix(CedarNodeType nodeType) {
    if (nodeType == CedarNodeType.USER) {
      return ldConfig.getUsersBase();
    } else {
      return ldConfig.getBase() + nodeType.getPrefix() + "/";
    }
  }

  public String getUserId(String uuid) {
    return getLinkedDataId(CedarNodeType.USER, uuid);
  }

  public String getLinkedDataId(CedarNodeType nodeType, String uuid) {
    return getLinkedDataPrefix(nodeType) + uuid;
  }

  public String buildNewLinkedDataId(CedarNodeType nodeType) {
    return getLinkedDataId(nodeType, UUID.randomUUID().toString());
  }

  public String getUUID(String resourceId, CedarNodeType nodeType) {
    String prefix = getLinkedDataPrefix(nodeType);
    if (resourceId != null && resourceId.startsWith(prefix)) {
      return resourceId.substring(prefix.length());
    } else {
      return null;
    }
  }

}