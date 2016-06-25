package org.metadatacenter.config;

public interface KeycloakConfig {
  String getClientId();

  KeycloakAdminUser getAdminUser();
}
