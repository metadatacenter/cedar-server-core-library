package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.error.CedarErrorPack;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CedarExceptionMapper implements ExceptionMapper<Exception> {

  public Response toResponse(Exception exception) {
    CedarErrorPack cep = new CedarErrorPack();
    cep.sourceException(exception);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(cep)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

}
