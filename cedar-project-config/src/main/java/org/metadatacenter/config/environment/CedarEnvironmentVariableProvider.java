package org.metadatacenter.config.environment;

import org.metadatacenter.model.SystemComponent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CedarEnvironmentVariableProvider {

  public static Map<String, String> getFor(SystemComponent useCase) {
    Map<String, String> env = new LinkedHashMap<>();
    List<CedarEnvironmentVariable> environmentVariables = CedarConfigEnvironmentDescriptor.getVariableNamesFor(useCase);
    for (CedarEnvironmentVariable var : environmentVariables) {
      String value = System.getenv(var.getName());
      env.put(var.getName(), value);
    }
    return env;
  }
}
