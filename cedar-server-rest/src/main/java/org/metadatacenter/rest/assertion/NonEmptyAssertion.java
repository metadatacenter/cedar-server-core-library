package org.metadatacenter.rest.assertion;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import org.metadatacenter.rest.CedarAssertionNoun;
import org.metadatacenter.rest.assertion.noun.CedarParameter;
import org.metadatacenter.rest.assertion.noun.CedarRequestBody;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.rest.exception.CedarAssertionResult;

public class NonEmptyAssertion implements CedarAssertion {

  NonEmptyAssertion() {
  }

  @Override
  public CedarAssertionResult check(CedarRequestContext requestContext, CedarAssertionNoun target) {
    if (!(target instanceof CedarParameter) && !(target instanceof CedarRequestBody)) {
      return new CedarAssertionResult(
          "Only instances of CedarParameter and CedarRequestBody can be checked with this assertion");
    }
    if (target instanceof CedarParameter) {
      CedarParameter cedarParameter = (CedarParameter) target;
      String s = cedarParameter.stringValue();
      if (s != null && !s.trim().isEmpty()) {
        return null;
      } else {
        return new CedarAssertionResult("You need to provide a non-null value for the parameter:"
            + cedarParameter.getName() + " from " + cedarParameter.getSource())
            .setParameter("name", cedarParameter.getName())
            .setParameter("source", cedarParameter.getSource())
            .badRequest();
      }
    } else {
      CedarRequestBody cedarRequestBody = (CedarRequestBody) target;
      JsonNode jsonNode = cedarRequestBody.asJson();
      if (jsonNode.isNull() || jsonNode.isMissingNode()) {
        return new CedarAssertionResult("You need to provide a non-null request body").badRequest();
      } else {
        return null;
      }
    }
  }

  @Override
  public CedarAssertionResult check(CedarRequestContext requestContext, Object target) {
    return new CedarAssertionResult("Not implemented for Objects");
  }

}
