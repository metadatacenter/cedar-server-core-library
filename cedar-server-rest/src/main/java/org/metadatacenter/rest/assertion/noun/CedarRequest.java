package org.metadatacenter.rest.assertion.noun;

import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.CedarAssertionNoun;

import java.util.Optional;

public interface CedarRequest extends CedarAssertionNoun {

  CedarRequestBody getRequestBody() throws CedarProcessingException;

  String getContentType();

  String getAuthorizationHeader();

  CedarParameter wrapQueryParam(String paramName, Optional<?> paramValue);

}
