package org.metadatacenter.server.play;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.server.result.BackendCallError;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.exception.MissingPermissionException;
import org.metadatacenter.util.json.JsonMapper;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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

  public static Result backendCallError(BackendCallResult backendCallResult) {
    if (backendCallResult != null) {
      List<BackendCallError> errors = backendCallResult.getErrors();
      if (errors != null && errors.size() > 0) {
        BackendCallError firstError = errors.get(0);
        if (firstError != null) {
          switch (firstError.getType()) {
            case AUTHENTICATION:
              return unauthorized(generateErrorDescription(backendCallResult));
            case AUTHORIZATION:
              return forbidden(generateErrorDescription(backendCallResult));
            case INVALID_ARGUMENT:
              return badRequest(generateErrorDescription(backendCallResult));
            case NOT_FOUND:
              return notFound(generateErrorDescription(backendCallResult));
            case SERVER_ERROR:
              return internalServerError(generateErrorDescription(backendCallResult));
          }
        }
      }
    }
    throw new IllegalArgumentException("backendErrorCall was requested with a non-error backendCallResult");
  }

  private static ObjectNode generateErrorDescription(BackendCallResult backendCallResult) {
    ObjectNode wrapper = JsonNodeFactory.instance.objectNode();
    ArrayNode errors = wrapper.putArray("errors");
    for (BackendCallError e : backendCallResult.getErrors()) {
      ObjectNode error = JsonNodeFactory.instance.objectNode();
      error.put("code", e.getCode());
      error.put("message", e.getMessage());
      error.put("type", e.getType().getValue());
      error.put("subType", e.getSubType());
      error.put("suggestedAction", e.getSuggestedAction());
      ObjectNode params = JsonNodeFactory.instance.objectNode();
      error.put("params", params);
      for (String paramName : e.getParams().keySet()) {
        Object param = e.getParams().get(paramName);
        params.set(paramName, JsonMapper.MAPPER.valueToTree(param));
      }
      errors.add(error);
    }
    return wrapper;
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
