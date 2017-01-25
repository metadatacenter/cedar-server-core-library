package org.metadatacenter.rest.context;

import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.server.security.model.user.CedarUser;

import javax.servlet.http.HttpServletRequest;

public class CedarRequestContextFactory {
  public static CedarRequestContext fromRequest(HttpServletRequest request) throws CedarAccessException {
    HttpServletRequestContext sc = new HttpServletRequestContext(request);
    if (sc.getUserCreationException() != null) {
      throw sc.getUserCreationException();
    }
    return sc;
  }

  public static CedarRequestContext fromUser(CedarUser user) throws CedarAccessException {
    LocalRequestContext lrc = new LocalRequestContext(user);
    if (lrc.getUserCreationException() != null) {
      throw lrc.getUserCreationException();
    }
    return lrc;
  }
}
