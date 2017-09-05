package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.exception.CedarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CedarCedarExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<CedarException> {

  private static final Logger log = LoggerFactory.getLogger(CedarCedarExceptionMapper.class);

  public Response toResponse(CedarException exception) {
    CedarErrorPack errorPack = exception.getErrorPack();
    hideExceptionConditionally(errorPack);
    if (exception.isShowFullStackTrace()) {
      log.warn(":CCEM:full:", exception);
    } else {
      log.warn(":CCEM:msg :" + exception.getMessage());
    }
    return Response.status(errorPack.getStatus())
        .entity(errorPack)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

}
