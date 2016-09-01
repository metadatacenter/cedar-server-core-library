package org.metadatacenter.config;

public interface FolderRESTAPIPagination extends ServerConfig {
  int getDefaultPageSize();
  int getMaxPageSize();
}
