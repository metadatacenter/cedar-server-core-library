package org.metadatacenter.rest.context;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.rest.assertion.noun.CedarRequestBody;
import org.metadatacenter.rest.assertion.noun.CedarRequestNoun;
import org.metadatacenter.rest.exception.CedarAssertionException;
import org.metadatacenter.util.json.JsonMapper;

import javax.servlet.http.HttpServletRequest;

public class NativeHttpServletRequest extends CedarRequestNoun {

  private final HttpServletRequest nativeRequest;

  NativeHttpServletRequest(HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("The HttpServletRequest should never be null at this point");
    }
    this.nativeRequest = request;
  }

  @Override
  public CedarRequestBody getRequestBody() throws CedarAssertionException {
    JsonNode jsonBodyNode = null;
    try {
      jsonBodyNode = JsonMapper.MAPPER.readTree(nativeRequest.getReader());
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (jsonBodyNode != null) {
      return new HttpRequestJsonBody(jsonBodyNode);
    } else {
      return new HttpRequestEmptyBody();
    }
  }

  @Override
  public String getContentType() {
    if (nativeRequest != null) {
      return nativeRequest.getHeader("Content-Type");
    }
    return null;
  }
}
