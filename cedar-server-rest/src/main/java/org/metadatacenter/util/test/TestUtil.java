package org.metadatacenter.util.test;


import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.security.CedarApiKeyAuthRequest;
import org.metadatacenter.server.security.model.AuthRequest;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public class TestUtil {

  public static String getTestUser1AuthHeader(CedarConfig cedarConfig) {
    String uuid = cedarConfig.getTestUsers().getTestUser1().getUuid();

    CedarUser user = null;

    try {
      user = CedarDataServices.getUserService().findUser(uuid);
    } catch (IOException e) {
      e.printStackTrace();
      // TODO: use import javax.ws.rs.ProcessingException
    } catch (com.github.fge.jsonschema.core.exceptions.ProcessingException e) {
      e.printStackTrace();
    }

    AuthRequest authRequest = new CedarApiKeyAuthRequest(user.getFirstActiveApiKey());
    return authRequest.getAuthHeader();
  }

}
