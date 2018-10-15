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

  private ServerName serverName;
  private AppLogType type;
  private AppLogSubType subType;
  private Instant time;
  private Duration duration;
  private Map<String, Object> parameters = new HashMap<>();

  public AppLogMessage() {
  }

  public AppLogMessage(ServerName serverName, AppLogType type, AppLogSubType subType) {
    this.time = Instant.now();
    this.serverName = serverName;
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

  public ServerName getServerName() {
    return serverName;
  }

  public AppLogType getType() {
    return type;
  }

  public AppLogSubType getSubType() {
    return subType;
  }

  public Instant getTime() {
    return time;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public Duration getDuration() {
    return duration;
  }
}
