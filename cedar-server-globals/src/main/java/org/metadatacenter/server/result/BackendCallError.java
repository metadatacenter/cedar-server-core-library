package org.metadatacenter.server.result;

import java.util.Map;
import java.util.HashMap;

public class BackendCallError {

  private BackendCallErrorType type;
  private String subType;
  private String message;
  private Map<String, Object> params;
  private String suggestedAction;
  private String code;

  BackendCallError(BackendCallErrorType type) {
    this.type = type;
    this.params = new HashMap<>();
  }

  public BackendCallErrorType getType() {
    return type;
  }

  public BackendCallError subType(String subType) {
    this.subType = subType;
    return this;
  }

  public String getSubType() {
    return subType;
  }

  public BackendCallError message(String message) {
    this.message = message;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public BackendCallError param(String paramName, Object paramValue) {
    this.params.put(paramName, paramValue);
    return this;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public BackendCallError action(String suggestedAction) {
    this.suggestedAction = suggestedAction;
    return this;
  }

  public String getSuggestedAction() {
    return suggestedAction;
  }

  public BackendCallError code(String code) {
    this.code = code;
    return this;
  }

  public String getCode() {
    return code;
  }
}
