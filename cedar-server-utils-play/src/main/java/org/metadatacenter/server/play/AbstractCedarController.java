package org.metadatacenter.server.play;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.exception.MissingPermissionException;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class AbstractCedarController extends Controller {

  protected static DateFormat xsdDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

  /* For CORS */
  public static Result preflight(String all) {
    response().setHeader("Access-Control-Allow-Origin", "*");
    response().setHeader("Allow", "*");
    response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
    response().setHeader("Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent, Authorization");
    return ok();
  }

  protected static ObjectNode generateErrorDescription(Throwable t) {
    ObjectNode errorParams = JsonNodeFactory.instance.objectNode();
    String errorSubType = "other";
    String suggestedAction = "none";
    String errorCode = "";

    ObjectNode errorDescription = JsonNodeFactory.instance.objectNode();
    if (t != null) {
      errorDescription.put("errorType", "exception");
      errorDescription.put("message", t.getMessage());
      errorDescription.put("localizedMessage", t.getLocalizedMessage());
      errorDescription.put("string", t.toString());
      if (t instanceof CedarAccessException) {
        errorSubType = "authException";
        errorCode = ((CedarAccessException) t).getErrorCode();
        suggestedAction = ((CedarAccessException) t).getSuggestedAction();
        if (t instanceof MissingPermissionException) {
          errorParams.put("missingPermission", ((MissingPermissionException) t).getPermissionName());
        }
      }
      ArrayNode jsonST = errorDescription.putArray("stackTrace");
      for (StackTraceElement ste : t.getStackTrace()) {
        jsonST.add(ste.toString());
      }
    }
    errorDescription.put("errorSubType", errorSubType);
    errorDescription.put("errorCode", errorCode);
    errorDescription.put("suggestedAction", suggestedAction);
    errorDescription.set("errorParams", errorParams);
    return errorDescription;
  }

  protected static ObjectNode generateErrorDescription(String errorSubType, String message) {
    return generateErrorDescription(errorSubType, message, null, null, null);
  }

  protected static ObjectNode generateErrorDescription(String errorSubType, String message, ObjectNode errorParams) {
    return generateErrorDescription(errorSubType, message, errorParams, null, null);
  }

  protected static ObjectNode generateErrorDescription(String errorSubType, String message, ObjectNode errorParams,
                                                       String suggestedAction, String errorCode) {
    if (errorParams == null) {
      errorParams = JsonNodeFactory.instance.objectNode();
    }
    ObjectNode errorDescription = JsonNodeFactory.instance.objectNode();
    errorDescription.put("message", message);
    errorDescription.put("errorSubType", errorSubType);
    errorDescription.put("errorCode", errorCode);
    errorDescription.put("suggestedAction", suggestedAction);
    errorDescription.set("errorParams", errorParams);
    return errorDescription;
  }

  protected static Result internalServerErrorWithError(Throwable t) {
    return internalServerError(generateErrorDescription(t));
  }

  protected static Result internalServerErrorWithError(ObjectNode on) {
    return internalServerError(on);
  }

  protected static Result badRequestWithError(Throwable t) {
    return badRequest(generateErrorDescription(t));
  }

  protected static Result forbiddenWithError(Throwable t) {
    return forbidden(generateErrorDescription(t));
  }

  protected static Result unauthorizedWithError(Throwable t) {
    return unauthorized(generateErrorDescription(t));
  }


}
