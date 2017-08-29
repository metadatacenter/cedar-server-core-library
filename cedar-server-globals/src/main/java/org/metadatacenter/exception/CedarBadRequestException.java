package org.metadatacenter.exception;

import org.metadatacenter.error.CedarErrorPack;

import javax.ws.rs.core.Response;

public class CedarBadRequestException extends CedarException {

  public CedarBadRequestException(CedarErrorPack ep) {
    super(ep);
    errorPack.status(Response.Status.BAD_REQUEST);
  }

  public CedarBadRequestException(String message) {
    super(message);
    errorPack.status(Response.Status.BAD_REQUEST);
  }
}
