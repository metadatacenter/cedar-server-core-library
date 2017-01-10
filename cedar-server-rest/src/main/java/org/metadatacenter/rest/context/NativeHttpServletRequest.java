package org.metadatacenter.rest.context;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.assertion.noun.CedarParameter;
import org.metadatacenter.rest.assertion.noun.CedarRequestBody;
import org.metadatacenter.rest.assertion.noun.CedarRequestNoun;
import org.metadatacenter.rest.assertion.noun.CedarWrappedQueryParameter;
import org.metadatacenter.util.json.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.util.Optional;

public class NativeHttpServletRequest extends CedarRequestNoun {

  private final HttpServletRequest nativeRequest;
  private JsonNode jsonBodyNode;

  NativeHttpServletRequest(HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("The HttpServletRequest should never be null at this point");
    }
    this.nativeRequest = request;
  }

  @Override
  public CedarRequestBody getRequestBody() throws CedarProcessingException {
    if (jsonBodyNode == null) {
      try {
        jsonBodyNode = JsonMapper.MAPPER.readTree(new InputStreamReader(nativeRequest.getInputStream()));
      } catch (Exception e) {
        throw new CedarProcessingException(e);
      }
    }

    //if (jsonBodyNode != null) {
    return new HttpRequestJsonBody(jsonBodyNode);
    /*} else {
      return new HttpRequestEmptyBody();
    }*/
  }

  @Override
  public String getContentType() {
    if (nativeRequest != null) {
      //TODO: use constant here
      return nativeRequest.getHeader("Content-Type");
    }
    return null;
  }

  @Override
  public String getHeader(String name) {
    return nativeRequest.getHeader(name);
  }

  @Override
  public CedarParameter wrapQueryParam(String paramName, Optional<? extends Object> paramValue) {
    return new CedarWrappedQueryParameter(paramName, paramValue);
  }
}
