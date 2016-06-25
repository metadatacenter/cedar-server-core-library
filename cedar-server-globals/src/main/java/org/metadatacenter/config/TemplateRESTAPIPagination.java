package org.metadatacenter.config;

public interface TemplateRESTAPIPagination extends ServerConfig {
  int getDefaultPageSize();
  int getMaxPageSize();
}
