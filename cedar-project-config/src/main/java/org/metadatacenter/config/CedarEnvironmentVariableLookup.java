package org.metadatacenter.config;

import io.dropwizard.configuration.UndefinedEnvironmentVariableException;
import org.apache.commons.lang3.text.StrLookup;
import org.metadatacenter.config.environment.CedarEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CedarEnvironmentVariableLookup extends StrLookup<Object> {

  private enum VariableStatus {
    PRESENT_WITH_VALUE, PRESENT_WITHOUT_VALUE, NEEDED_NOT_INCLUDED;
  }

  private static final Logger log = LoggerFactory.getLogger(CedarEnvironmentVariableLookup.class);

  private final boolean strict;
  private final Map<String, String> environment;

  public CedarEnvironmentVariableLookup(Map<String, String> environment, boolean strict) {
    this.environment = environment;
    this.strict = strict;

    Map<String, VariableStatus> status = new LinkedHashMap<>();
    List<String> namesWithNullValue = new ArrayList<>();
    for (CedarEnvironmentVariable ev : CedarEnvironmentVariable.values()) {
      String name = ev.getName();
      if (!environment.containsKey(name)) {
        status.put(name, VariableStatus.NEEDED_NOT_INCLUDED);
      } else {
        String v = environment.get(name);
        if (v == null) {
          status.put(name, VariableStatus.PRESENT_WITHOUT_VALUE);
          namesWithNullValue.add(name);
        } else {
          status.put(name, VariableStatus.PRESENT_WITH_VALUE);
        }
      }
    }

    log.info("----------------------------------------------------------------------------------------");
    log.info("------------------------- Environment variable sandbox ---------------------------------");
    log.info("With values: ---------------------------------------------------------------------------");
    for (String name : status.keySet()) {
      VariableStatus stat = status.get(name);
      if (stat == VariableStatus.PRESENT_WITH_VALUE) {
        log.info("---- " + name);
      }
    }
    log.info("Without values: ------------------------------------------------------------------------");
    for (String name : status.keySet()) {
      VariableStatus stat = status.get(name);
      if (stat == VariableStatus.PRESENT_WITHOUT_VALUE) {
        log.info("---- " + name);
      }
    }
    log.info("Not included in this sandbox: ----------------------------------------------------------");
    for (String name : status.keySet()) {
      VariableStatus stat = status.get(name);
      if (stat == VariableStatus.NEEDED_NOT_INCLUDED) {
        log.info("---- " + name);
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
