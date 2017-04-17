package org.metadatacenter.config.environment;

import org.metadatacenter.model.SystemComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CedarEnvironmentVariableProvider {

  private static final Logger log = LoggerFactory.getLogger(CedarEnvironmentVariableProvider.class);

  public static Map<String, String> getFor(SystemComponent useCase) {
    Set<CedarEnvironmentVariable> neededVariables = CedarConfigEnvironmentDescriptor.getVariableNamesFor(useCase);
    Map<String, String> env = new LinkedHashMap<>();
    for (CedarEnvironmentVariable variable : CedarEnvironmentVariable.values()) {
      if (neededVariables.contains(variable)) {
        String value = System.getenv(variable.getName());
        env.put(variable.getName(), value);
      } else {
        log.info("Environment contains not needed variable, holding it back from sandbox: " + variable.getName());
      }
    }
    return env;
  }
}
