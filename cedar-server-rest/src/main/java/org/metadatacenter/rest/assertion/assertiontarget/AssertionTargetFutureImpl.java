package org.metadatacenter.rest.assertion.assertiontarget;

import org.metadatacenter.rest.CedarAssertionNoun;
import org.metadatacenter.operation.CedarOperationDescriptor;
import org.metadatacenter.rest.assertion.CedarAssertion;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.rest.exception.CedarAssertionException;
import org.metadatacenter.error.CedarAssertionResult;

import javax.ws.rs.core.Response;
import java.util.Collection;

public abstract class AssertionTargetFutureImpl<T> implements AssertionTargetFuture {

  protected Collection<T> targets;
  protected CedarRequestContext requestContext;
  protected Collection<CedarAssertion> assertions;

  @Override
  public void otherwiseBadRequest() throws CedarAssertionException {
    buildAndThrowAssertionExceptionIfNeeded(getFirstAssertionError(), null, null, Response.Status.BAD_REQUEST);
  }

  @Override
  public void otherwiseBadRequest(CedarOperationDescriptor operation, String message) throws CedarAssertionException {
    buildAndThrowAssertionExceptionIfNeeded(getFirstAssertionError(), operation, message, Response.Status.BAD_REQUEST);
  }

  @Override
  public void otherwiseInternalServerError(CedarOperationDescriptor operation, String message) throws
      CedarAssertionException {
    buildAndThrowAssertionExceptionIfNeeded(getFirstAssertionError(), operation, message, Response.Status
        .INTERNAL_SERVER_ERROR);
  }

  @Override
  public void otherwiseNotFound(CedarOperationDescriptor operation, String message) throws CedarAssertionException {
    buildAndThrowAssertionExceptionIfNeeded(getFirstAssertionError(), operation, message, Response.Status.NOT_FOUND);
  }

  @Override
  public void otherwiseForbidden(CedarOperationDescriptor operation, String message) throws CedarAssertionException {
    buildAndThrowAssertionExceptionIfNeeded(getFirstAssertionError(), operation, message, Response.Status.FORBIDDEN);
  }

  protected CedarAssertionResult getFirstAssertionError() {
    CedarAssertionResult assertionError;
    for (T target : targets) {
      for (CedarAssertion assertion : assertions) {
        if (target instanceof CedarAssertionNoun) {
          assertionError = assertion.check(requestContext, (CedarAssertionNoun) target);
        } else {
          assertionError = assertion.check(requestContext, target);
        }
        if (assertionError != null) {
          return assertionError;
        }
      }
    }
    return null;
  }

  private void buildAndThrowAssertionExceptionIfNeeded(CedarAssertionResult assertionResult,
                                                       CedarOperationDescriptor operation, String message,
                                                       Response.Status status) throws CedarAssertionException {
    if (assertionResult != null) {
      assertionResult.status(status);
      if (message != null) {
        assertionResult.message(message);
      }
      throw new CedarAssertionException(assertionResult, operation);
    }
  }


}
