package org.metadatacenter.server.security;

import org.keycloak.adapters.KeycloakDeployment;

public class KeycloakDeploymentProvider {

  private static final KeycloakDeploymentProvider instance = new KeycloakDeploymentProvider();

  private final KeycloakDeployment keycloakDeployment;

  private KeycloakDeploymentProvider() {
    keycloakDeployment = KeycloakUtils.buildDeployment();
  }

  public static KeycloakDeploymentProvider getInstance() {
    return instance;
  }

  public KeycloakDeployment getKeycloakDeployment() {
    return keycloakDeployment;
  }
}
