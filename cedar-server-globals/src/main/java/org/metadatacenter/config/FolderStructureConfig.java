package org.metadatacenter.config;

public interface FolderStructureConfig {
  GraphFolder getRootFolder();

  GraphFolder getUsersFolder();

  GraphFolder getLostAndFoundFolder();
}
