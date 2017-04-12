package org.metadatacenter.config;

import io.dropwizard.configuration.UndefinedEnvironmentVariableException;
import org.apache.commons.lang3.text.StrLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarEnvironmentVariableLookup extends StrLookup<Object> {

  private static final Logger log = LoggerFactory.getLogger(CedarEnvironmentVariableLookup.class);

  private final boolean strict;

  public CedarEnvironmentVariableLookup(boolean strict) {
    this.strict = strict;
  }

  public String lookup(String key) {
    String value = System.getenv(key);
    if (value == null) {
      if (this.strict) {
        throw new UndefinedEnvironmentVariableException("The environment variable '" + key + "' is not defined; " +
            "could not substitute the expression '${" + key + "}'!");
      } else {
        log.warn("Environment variable '" + key + "' is missing, but we are not in strict mode.");
      }
    }
    return value;
  }
}
