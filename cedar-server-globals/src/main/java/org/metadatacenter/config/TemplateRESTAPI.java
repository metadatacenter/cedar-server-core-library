package org.metadatacenter.config;

import java.util.List;

public interface TemplateRESTAPI extends ServerConfig {
  TemplateRESTAPIPagination getPagination();

  List<String> getExcludedFields();
}
