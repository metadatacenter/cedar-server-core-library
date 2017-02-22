package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.error.CedarErrorPack;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public abstract class AbstractExceptionMapper {

  protected
  @Context
  HttpServletRequest request;

  protected boolean hideExceptionConditionally(CedarErrorPack errorPack) {
    if (!"true".equals(request.getParameter("debug")) &&
        !"true".equals(request.getHeader(CedarConstants.HTTP_HEADER_DEBUG))) {
      errorPack.resetSourceException();
      return true;
    } else {
      return false;
    }
  }

}
