package org.metadatacenter.server.security;

import org.keycloak.adapters.KeycloakDeployment;
import org.metadatacenter.config.CedarConfig;

public class KeycloakDeploymentProvider {

  private static final KeycloakDeploymentProvider instance = new KeycloakDeploymentProvider();

  private KeycloakDeploymentProvider() {
    CedarConfig cedarConfig = CedarConfig.getInstance();
    KeycloakDeployment keycloakDeployment = KeycloakUtils.buildDeployment(cedarConfig.getKeycloakConfig()
        .getConfigFile());
  }

  public static KeycloakDeploymentProvider getInstance() {
    return instance;
  }

}
