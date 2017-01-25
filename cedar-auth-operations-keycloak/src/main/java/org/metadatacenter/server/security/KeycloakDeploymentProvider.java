package org.metadatacenter.server.security;

import org.keycloak.adapters.KeycloakDeployment;

public class KeycloakDeploymentProvider {

  private static final KeycloakDeploymentProvider instance = new KeycloakDeploymentProvider();

  private final KeycloakDeployment keycloakDeployment;

  private KeycloakDeploymentProvider() {
    String keycloakConfigPath = System.getProperty("keycloak.config.path");
    if (keycloakConfigPath == null || "".equals(keycloakConfigPath)) {
      keycloakDeployment = KeycloakUtils.buildDeployment();
    } else {
      keycloakDeployment = KeycloakUtils.buildDeployment(keycloakConfigPath);
    }
  }

  public static KeycloakDeploymentProvider getInstance() {
    return instance;
  }

  public KeycloakDeployment getKeycloakDeployment() {
    return keycloakDeployment;
  }
}
