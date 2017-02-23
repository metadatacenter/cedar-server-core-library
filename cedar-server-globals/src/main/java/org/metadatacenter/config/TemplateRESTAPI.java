package org.metadatacenter.config;

import java.util.List;

public class TemplateRESTAPI {

  private PaginationConfig pagination;

  private List<String> excludedFields;

  private TemplateRESTAPISummaries summaries;

  public PaginationConfig getPagination() {
    return pagination;
  }

  public List<String> getExcludedFields() {
    return excludedFields;
  }

  public TemplateRESTAPISummaries getSummaries() {
    return summaries;
  }
}
