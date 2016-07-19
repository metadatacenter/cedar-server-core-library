package org.metadatacenter.config;

import java.util.List;
import java.util.Map;

public interface BlueprintUserProfile {
  String getScreenNameTemplate();

  String getHomeFolderDescriptionTemplate();

  BlueprintDefaultAPIKey getDefaultAPIKey();

  Map<String, List<String>> getUserRoles();
}
