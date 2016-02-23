package org.metadatacenter.server.play;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.exception.MissingRealmRoleException;
import play.mvc.Controller;
import play.mvc.Result;

public abstract class AbstractCedarController extends Controller {

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
    errorDescription.put("errorType", "exception");
    errorDescription.put("message", t.getMessage());
    errorDescription.put("localizedMessage", t.getLocalizedMessage());
    errorDescription.put("string", t.toString());
    if (t instanceof CedarAccessException) {
      errorSubType = "authException";
      errorCode = ((CedarAccessException) t).getErrorCode();
      suggestedAction = ((CedarAccessException) t).getSuggestedAction();
      if (t instanceof MissingRealmRoleException) {
        errorParams.put("missingRole", ((MissingRealmRoleException) t).getRoleName());
      }
    }
    errorDescription.put("errorSubType", errorSubType);
    errorDescription.put("errorCode", errorCode);
    errorDescription.put("suggestedAction", suggestedAction);
    errorDescription.set("errorParams", errorParams);
    ArrayNode jsonST = errorDescription.putArray("stackTrace");
    for (StackTraceElement ste : t.getStackTrace()) {
      jsonST.add(ste.toString());
    }
    return errorDescription;
  }

  protected static Result internalServerErrorWithError(Throwable t) {
    return internalServerError(generateErrorDescription(t));
  }

  protected static Result badRequestWithError(Throwable t) {
    return badRequest(generateErrorDescription(t));
  }

  protected static Result forbiddenWithError(Throwable t) {
    return forbidden(generateErrorDescription(t));
  }

}
