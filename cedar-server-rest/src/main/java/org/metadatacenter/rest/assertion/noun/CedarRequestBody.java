package org.metadatacenter.rest.assertion.noun;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.rest.CedarAssertionNoun;
import org.metadatacenter.rest.exception.CedarAssertionException;

public interface CedarRequestBody extends CedarAssertionNoun {

  CedarParameter get(String name);

  JsonNode asJson();

  <T> T convert(Class<T> type) throws CedarAssertionException;
}
