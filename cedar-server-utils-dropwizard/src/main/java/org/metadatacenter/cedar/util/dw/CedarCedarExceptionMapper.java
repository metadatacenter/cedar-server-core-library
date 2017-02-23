package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.exception.CedarException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CedarCedarExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<CedarException> {

  public Response toResponse(CedarException exception) {
    CedarErrorPack errorPack = exception.getErrorPack();
    hideExceptionConditionally(errorPack);
    return Response.status(errorPack.getStatus())
        .entity(errorPack)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

}
