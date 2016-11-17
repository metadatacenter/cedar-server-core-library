package org.metadatacenter.rest.context;

import org.metadatacenter.server.security.model.user.CedarUser;
import play.mvc.Http;

import javax.servlet.http.HttpServletRequest;

public class CedarRequestContextFactory {
  public static CedarRequestContext fromRequest(Http.Request request) {
    return new PlayRequestContext(request);
  }

  public static CedarRequestContext fromRequest(HttpServletRequest request) {
    return new HttpServletRequestContext(request);
  }

  public static CedarRequestContext fromUser(CedarUser user) {
    return new PlayRequestContext(user);
  }
}
