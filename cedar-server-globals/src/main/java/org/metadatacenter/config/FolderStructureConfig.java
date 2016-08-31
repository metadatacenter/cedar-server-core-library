package org.metadatacenter.config;

public interface FolderStructureConfig {
  GraphFolder getRootFolder();

  GraphFolder getUsersFolder();

  GraphGroup getEverybodyGroup();
}
