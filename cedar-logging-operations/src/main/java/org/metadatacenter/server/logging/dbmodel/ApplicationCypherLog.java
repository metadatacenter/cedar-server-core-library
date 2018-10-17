package org.metadatacenter.server.logging.dbmodel;

import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.logging.model.AppLogParam;

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

  private Instant startTime;

  private Instant endTime;

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

  private int lineNumber;

  @Column(length = 30)
  private String operation;

  @Column(length = 2048)
  private String queryParameters;

  @Column(length = 32)
  private String runnableHash;

  @Column(length = 32)
  private String parametersHash;


  public static ApplicationCypherLog fromAppCypherLog(AppLogMessage appLog) {
    ApplicationCypherLog l = new ApplicationCypherLog();
    l.requestId = appLog.getRequestId();
    l.serverName = appLog.getServerName().getName();
    l.original = appLog.getParamAsString(AppLogParam.ORIGINAL_QUERY);
    l.runnable = appLog.getParamAsString(AppLogParam.RUNNABLE_QUERY);
    l.interpolated = appLog.getParamAsString(AppLogParam.INTERPOLATED_QUERY);
    l.runnableHash = appLog.getParamAsString(AppLogParam.RUNNABLE_QUERY_HASH);
    l.parametersHash = appLog.getParamAsString(AppLogParam.QUERY_PARAMETERS_HASH);
    l.time = appLog.getTime();
    l.duration = appLog.getDuration();
    l.startTime = appLog.getParamAsInstant(AppLogParam.START_TIME);
    l.endTime = appLog.getParamAsInstant(AppLogParam.END_TIME);
    l.className = appLog.getParamAsString(AppLogParam.CLASS_NAME);
    l.methodName = appLog.getParamAsString(AppLogParam.METHOD_NAME);
    l.lineNumber = appLog.getParamAsInt(AppLogParam.LINE_NUMBER);
    l.operation = appLog.getParamAsString(AppLogParam.OPERATION);
    l.queryParameters = appLog.getParamAsString(AppLogParam.QUERY_PARAMETERS);
    return l;
  }

  public Long getId() {
    return id;
  }
}
