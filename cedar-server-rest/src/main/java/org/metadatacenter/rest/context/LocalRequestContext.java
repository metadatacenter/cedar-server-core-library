package org.metadatacenter.rest.context;

import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.rest.assertion.noun.CedarUserNoun;
import org.metadatacenter.server.security.model.user.CedarUser;

public class LocalRequestContext extends AbstractRequestContext {

  private CedarAccessException userCreationException;

  LocalRequestContext(CedarUser cedarUser) {
    currentUser = cedarUser;
    wrappedRequest = new LocalRequest(currentUser);
    user = new CedarUserNoun(currentUser);
  }

  public CedarAccessException getUserCreationException() {
    return userCreationException;
  }


}
