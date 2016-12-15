package org.metadatacenter.rest.context;

import org.metadatacenter.rest.assertion.noun.CedarUserNoun;
import org.metadatacenter.server.security.model.user.CedarUser;

public class LocalRequestContext extends AbstractRequestContext {

  LocalRequestContext(CedarUser cedarUser) {
    currentUser = cedarUser;
    wrappedRequest = null;
    user = new CedarUserNoun(currentUser);
  }

}
