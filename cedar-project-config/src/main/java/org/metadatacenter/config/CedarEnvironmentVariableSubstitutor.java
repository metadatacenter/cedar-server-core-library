package org.metadatacenter.config;

import org.apache.commons.lang3.text.StrSubstitutor;

public class CedarEnvironmentVariableSubstitutor extends StrSubstitutor {

  public CedarEnvironmentVariableSubstitutor() {
    super(new CedarEnvironmentVariableLookup(true));
    this.setEnableSubstitutionInVariables(false);
  }
}
