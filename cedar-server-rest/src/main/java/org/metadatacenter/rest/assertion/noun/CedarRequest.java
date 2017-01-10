package org.metadatacenter.rest.assertion.noun;

import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.CedarAssertionNoun;
import org.metadatacenter.rest.exception.CedarAssertionException;

import java.util.Optional;

public interface CedarRequest extends CedarAssertionNoun {

  CedarRequestBody getRequestBody() throws CedarProcessingException;

  String getContentType();

  String getHeader(String name);

  CedarParameter wrapQueryParam(String paramName, Optional<? extends Object> paramValue);

}
