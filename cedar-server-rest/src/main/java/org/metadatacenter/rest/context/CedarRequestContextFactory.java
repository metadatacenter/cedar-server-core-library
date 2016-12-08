package org.metadatacenter.rest.context;

import javax.servlet.http.HttpServletRequest;

public class CedarRequestContextFactory {
  public static CedarRequestContext fromRequest(HttpServletRequest request) {
    return new HttpServletRequestContext(request);
  }

  /*public static CedarRequestContext fromUser(CedarUser user) {
    return new PlayRequestContext(user);
  }*/
}
