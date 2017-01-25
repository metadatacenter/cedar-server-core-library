package org.metadatacenter.config;

import java.util.List;

public class TemplateRESTAPI extends ServerConfig {

  private TemplateRESTAPIPagination pagination;

  private List<String> excludedFields;

  public TemplateRESTAPIPagination getPagination() {
    return pagination;
  }

  public List<String> getExcludedFields() {
    return excludedFields;
  }
}
