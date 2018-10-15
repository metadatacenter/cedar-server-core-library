package org.metadatacenter.server.logging.dbmodel;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "log_request")
public class ApplicationRequestLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 36)
  private String requestId;

  @Column(length = 50)
  private String serverName;

  @Column(length = 50)
  private String type;

  @Column(length = 50)
  private String subType;

  private Instant time;

  private Duration duration;

  @Column(length = 1024)
  private String methodName;

  @Column(length = 1024)
  private String className;

  @Lob
  private String parameters;

  public void setId(Long id) {
    this.id = id;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public Long getId() {
    return id;
  }
}
