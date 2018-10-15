package org.metadatacenter.server.logging.dbmodel;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "log_cypher")
public class ApplicationCypherLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 36)
  private String requestId;

  @Column(length = 50)
  private String serverName;

  private Instant time;

  private Duration duration;

  @Lob
  private String original;

  @Lob
  private String runnable;

  @Lob
  private String interpolated;

  @Lob
  private String parameters;

  @Column(length = 1024)
  private String methodName;

  @Column(length = 1024)
  private String className;

  @Column(length = 30)
  private String operation;

  public void setId(Long id) {
    this.id = id;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public void setRunnable(String runnable) {
    this.runnable = runnable;
  }

  public void setInterpolated(String interpolated) {
    this.interpolated = interpolated;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public Long getId() {
    return id;
  }
}
