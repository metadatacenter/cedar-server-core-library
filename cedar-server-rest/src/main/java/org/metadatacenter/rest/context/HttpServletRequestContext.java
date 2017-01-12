package org.metadatacenter.rest.context;

import org.metadatacenter.rest.assertion.noun.CedarUserNoun;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.CedarAuthFromRequestFactory;
import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.server.security.model.user.CedarUser;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestContext extends AbstractRequestContext {

  private final HttpServletRequest httpRequest;
  private CedarAccessException userCreationException;

  HttpServletRequestContext(HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("The HttpServletRequest should never be null at this point");
    }
    httpRequest = request;
    wrappedRequest = new NativeHttpServletRequest(request);
    try {
      authRequest = CedarAuthFromRequestFactory.fromRequest(request);
      currentUser = Authorization.getUser(authRequest);
    } catch (CedarAccessException e) {
      userCreationException = e;
    }
    user = new CedarUserNoun(currentUser);
  }

  HttpServletRequestContext(CedarUser cedarUser) {
    currentUser = cedarUser;
    httpRequest = null;
    wrappedRequest = null;
    user = new CedarUserNoun(currentUser);
  }

  public CedarAccessException getUserCreationException() {
    return userCreationException;
  }
}
