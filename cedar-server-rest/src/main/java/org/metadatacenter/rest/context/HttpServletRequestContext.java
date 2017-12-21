package org.metadatacenter.rest.context;

import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.rest.assertion.noun.CedarUserNoun;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.CedarAuthFromRequestFactory;
import org.metadatacenter.server.security.model.AuthRequest;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestContext extends AbstractRequestContext {

  HttpServletRequestContext(LinkedDataUtil linkedDataUtil, HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("The HttpServletRequest should never be null at this point");
    }
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
}
