package org.metadatacenter.rest.context;

import org.metadatacenter.constant.CedarHeaderParameters;
import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.rest.assertion.noun.CedarUserNoun;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.CedarAuthFromRequestFactory;
import org.metadatacenter.server.security.model.AuthRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

public class HttpServletRequestContext extends AbstractRequestContext {

  private HttpHeaders httpHeaders;

  public HttpServletRequestContext(LinkedDataUtil linkedDataUtil, HttpServletRequest request, HttpHeaders httpHeaders) {
    if (request == null) {
      throw new IllegalArgumentException("The HttpServletRequest should never be null at this point");
    }
    this.httpHeaders = httpHeaders;
    wrappedRequest = new NativeHttpServletRequest(request);
    try {
      AuthRequest authRequest = CedarAuthFromRequestFactory.fromRequest(request);
      currentUser = Authorization.getUser(linkedDataUtil, authRequest);
    } catch (CedarAccessException e) {
      userCreationException = e;
    }
    user = new CedarUserNoun(currentUser);
    this.linkedDataUtil = linkedDataUtil;
  }

  /*public HttpServletRequestContext(LinkedDataUtil linkedDataUtil, HttpServletRequest request) {
    this(linkedDataUtil, request, null);
  }*/

  @Override
  public String getAuthorizationHeader() {
    return httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
  }

  @Override
  public String getDebugHeader() {
    return httpHeaders.getHeaderString(CedarHeaderParameters.DEBUG);
  }

  @Override
  public String getRequestIdHeader() {
    return httpHeaders.getHeaderString(CedarHeaderParameters.REQUEST_ID_KEY);
  }
}
