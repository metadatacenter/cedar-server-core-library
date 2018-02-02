package org.metadatacenter.config;

import java.util.HashMap;

public class ElasticsearchSearchIndexMappingsConfig {

  private HashMap<String, Object> node;

  private HashMap<String, Object> users;

  private HashMap<String, Object> groups;

  private HashMap<String, Object> content;

  public HashMap<String, Object> getNode() {
    return node;
  }

  public HashMap<String, Object> getUsers() {
    return users;
  }

  public HashMap<String, Object> getGroups() {
    return groups;
  }

  public HashMap<String, Object> getContent() {
    return content;
  }
}