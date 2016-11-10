package org.metadatacenter.rest.context;

import org.metadatacenter.server.security.model.user.CedarUser;
import play.mvc.Http;

public class CedarRequestContextFactory {
  public static CedarRequestContext fromRequest(Http.Request request) {
    return new PlayRequestContext(request);
  }

  public static CedarRequestContext fromUser(CedarUser user) {
    return new PlayRequestContext(user);
  }
}
