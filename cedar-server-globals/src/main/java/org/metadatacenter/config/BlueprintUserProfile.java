package org.metadatacenter.config;

import java.util.List;
import java.util.Map;

public interface BlueprintUserProfile {
  BlueprintDefaultAPIKey getDefaultAPIKey();

  Map<String, List<String>> getUserRoles();
}
