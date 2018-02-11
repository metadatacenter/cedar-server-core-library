package org.metadatacenter.rest.assertion;

import org.apache.commons.validator.routines.UrlValidator;
import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.rest.CedarAssertionNoun;
import org.metadatacenter.rest.context.CedarRequestContext;

public class ValidUrlAssertion implements CedarAssertion {

  private final static UrlValidator urlValidator;

  static {
    String[] schemes = {"http", "https"};
    urlValidator = new UrlValidator(schemes);
  }

  ValidUrlAssertion() {
  }

  @Override
  public CedarAssertionResult check(CedarRequestContext requestContext, CedarAssertionNoun target) {
    // we only check strings
    return null;
  }

  @Override
  public CedarAssertionResult check(CedarRequestContext requestContext, Object target) {
    if (target == null || !(target instanceof String)) {
      return new CedarAssertionResult("The id should be a non-null String");
    } else {
      String id = (String) target;
      if (!urlValidator.isValid(id)) {
        return new CedarAssertionResult("The id is invalid:" + id).badRequest();
      }
      return null;
    }
  }

}
