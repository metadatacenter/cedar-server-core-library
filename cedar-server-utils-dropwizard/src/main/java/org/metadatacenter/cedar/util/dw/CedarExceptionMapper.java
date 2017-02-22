package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.error.CedarErrorPack;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CedarExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Exception> {

  public Response toResponse(Exception exception) {
    CedarErrorPack errorPack = new CedarErrorPack();
    if (!hideExceptionConditionally(errorPack)) {
      errorPack.sourceException(exception);
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(errorPack)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

}
