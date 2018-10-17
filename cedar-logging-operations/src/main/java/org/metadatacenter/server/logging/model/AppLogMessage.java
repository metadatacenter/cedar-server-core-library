package org.metadatacenter.server.logging.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.ServerName;
import org.metadatacenter.server.logging.AppLogger;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppLogMessage {

  private String requestId;
  private ServerName serverName;
  private AppLogType type;
  private AppLogSubType subType;
  private Instant time;
  private Duration duration;
  private Map<String, Object> parameters = new HashMap<>();

  public AppLogMessage() {
  }

  public AppLogMessage(ServerName serverName, String requestId, AppLogType type, AppLogSubType subType) {
    this.time = Instant.now();
    this.serverName = serverName;
    this.requestId = requestId;
    this.type = type;
    this.subType = subType;
  }

  public AppLogMessage param(AppLogParam param, Object value) {
    this.parameters.put(param.getValue(), value);
    return this;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void enqueue() {
    AppLogger.enqueue(this);
  }

  public String getRequestId() {
    return requestId;
  }

  public ServerName getServerName() {
    return serverName;
  }

  public AppLogType getType() {
    return type;
  }

  public AppLogSubType getSubType() {
    return subType;
  }

  public String getParamAsString(AppLogParam paramName) {
    return (String)parameters.get(paramName.getValue());
  }

  public Instant getParamAsInstant(AppLogParam paramName) {
    return (Instant)parameters.get(paramName.getValue());
  }

  public int getParamAsInt(AppLogParam paramName) {
    Object o = parameters.get(paramName.getValue());
    if (o == null) {
      return 0;
    } else {
      return (Integer)o;
    }
  }

  public Instant getTime() {
    return time;
  }

  public Duration getDuration() {
    return duration;
  }
}
