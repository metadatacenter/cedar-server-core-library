package org.metadatacenter.rest.context;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.assertion.noun.CedarRequestBody;
import org.metadatacenter.rest.assertion.noun.CedarRequestNoun;
import org.metadatacenter.util.json.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;

import static org.metadatacenter.constant.HttpConstants.HTTP_HEADER_AUTHORIZATION;
import static org.metadatacenter.constant.HttpConstants.HTTP_HEADER_CONTENT_TYPE;

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

    return new HttpRequestJsonBody(jsonBodyNode);
  }

  @Override
  public String getContentType() {
    if (nativeRequest != null) {
      return nativeRequest.getHeader(HTTP_HEADER_CONTENT_TYPE);
    }
    return null;
  }

  @Override
  public String getAuthorizationHeader() {
    return nativeRequest.getHeader(HTTP_HEADER_AUTHORIZATION);
  }

}
