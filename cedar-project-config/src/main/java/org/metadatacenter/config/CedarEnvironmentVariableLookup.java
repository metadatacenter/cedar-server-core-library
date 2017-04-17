package org.metadatacenter.config;

import io.dropwizard.configuration.UndefinedEnvironmentVariableException;
import org.apache.commons.lang3.text.StrLookup;
import org.metadatacenter.config.environment.CedarEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CedarEnvironmentVariableLookup extends StrLookup<Object> {

  private static final Logger log = LoggerFactory.getLogger(CedarEnvironmentVariableLookup.class);

  private final boolean strict;
  private final Map<String, String> environment;

  public CedarEnvironmentVariableLookup(Map<String, String> environment, boolean strict) {
    this.environment = environment;
    this.strict = strict;
    List<String> namesWithNullValue = new ArrayList<>();
    log.info("----------------------------------------------------------------------------------------");
    log.info("------------------------- Environment variable sandbox ---------------------------------");
    log.info("With values: ---------------------------------------------------------------------------");
    for (String name : environment.keySet()) {
      String v = environment.get(name);
      if (v != null) {
        log.info("---- " + name);
      }
    }
    log.info("Without values: ------------------------------------------------------------------------");
    for (String name : environment.keySet()) {
      String v = environment.get(name);
      if (v == null) {
        log.info("---- " + name);
        namesWithNullValue.add(name);
      }
    }
    log.info("Not included in this sandbox: ----------------------------------------------------------");
    for (CedarEnvironmentVariable ev : CedarEnvironmentVariable.values()) {
      if (!environment.containsKey(ev.getName())) {
        log.info("---- " + ev.getName());
      }
    }
    log.info("----------------------------------------------------------------------------------------");
    if (!namesWithNullValue.isEmpty()) {
      throw new UndefinedEnvironmentVariableException("The following environment variables are expected to be present" +
          " with a non-null value in the sandbox: " +
          namesWithNullValue +
          " The application can not continue under these circumstances!");
    }
  }

  public String lookup(String key) {
    String value = environment.get(key);
    if (value == null) {
      if (this.strict) {
        throw new UndefinedEnvironmentVariableException("The environment variable '" + key + "' is not defined; " +
            "could not substitute the expression '${" + key + "}'!");
      } else {
        log.debug("Environment variable missing, but we are in relaxed mode: '" + key + "'");
      }
    }
    return value;
  }
}
