package org.metadatacenter.server.security;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.common.util.Base64Url;
import org.keycloak.representations.AccessToken;
import org.keycloak.util.JsonSerialization;
import org.metadatacenter.constant.KeycloakConstants;
import org.metadatacenter.exception.security.AccessTokenExpiredException;
import org.metadatacenter.exception.security.AccessTokenMissingException;
import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.exception.security.InvalidOfflineAccessTokenException;
import org.metadatacenter.server.security.model.AuthRequest;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class KeycloakUtils {

  private static final Logger log = LoggerFactory.getLogger(KeycloakUtils.class);

  public static <T> T parseToken(String encoded, Class<T> clazz) throws IOException {
    if (encoded == null) {
      return null;
    }

    String[] parts = encoded.split("\\.");
    if (parts.length < 2 || parts.length > 3) {
      throw new IllegalArgumentException("Parsing error");
    }

    byte[] bytes = Base64Url.decode(parts[1]);
    return JsonSerialization.readValue(bytes, clazz);
  }

  public static KeycloakDeployment buildDeployment() {
    InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(KeycloakConstants.JSON);
    return KeycloakDeploymentBuilder.build(config);
  }

  public static KeycloakDeployment buildDeployment(String path) {
    try {
      return KeycloakDeploymentBuilder.build(new FileInputStream(Paths.get(path).toFile()));
    } catch (FileNotFoundException e) {
      log.error("File not found: " + path, e);
    }
    return null;
  }

  private static AccessToken checkIfTokenIsStillActiveByUserInfo(String token) throws CedarAccessException {
    AccessToken accessToken;
    if (token == null) {
      throw new AccessTokenMissingException();
    }
    try {
      accessToken = KeycloakUtils.parseToken(token, AccessToken.class);
    } catch (IOException e) {
      throw new InvalidOfflineAccessTokenException();
    }
    if (accessToken == null) {
      throw new InvalidOfflineAccessTokenException();
    } else if (accessToken.isExpired()) {
      throw new AccessTokenExpiredException(accessToken.getExpiration());
    } else {
      return accessToken;
    }
  }

  public static CedarUser getUserFromAuthRequest(AuthRequest authRequest, IUserService userService) throws
      CedarAccessException {
    String token = authRequest.getAuthString();
    AccessToken accessToken = checkIfTokenIsStillActiveByUserInfo(token);
    String userId = accessToken.getSubject();
    CedarUser user = null;
    try {
      user = userService.findUser(userId);
    } catch (IOException | ProcessingException e) {
      log.error("Error while getting user", e);
    }
    return user;
  }
}