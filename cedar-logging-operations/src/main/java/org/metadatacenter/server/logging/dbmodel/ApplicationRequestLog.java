package org.metadatacenter.server.logging.dbmodel;

import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.logging.model.AppLogParam;

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

  @Column(length = 20)
  private String requestIdSource;

  private Instant time;

  private Duration duration;

  @Column(length = 1024)
  private String methodName;

  @Column(length = 1024)
  private String className;

  private int lineNumber;

  @Column(length = 20)
  private String httpMethod;

  @Column(length = 2048)
  private String path;

  @Column(length = 2048)
  private String queryParameters;

  public static ApplicationRequestLog fromAppRequestFilter(AppLogMessage appLog) {
    ApplicationRequestLog l = new ApplicationRequestLog();
    l.requestId = appLog.getRequestId();
    l.serverName = appLog.getServerName().getName();
    l.type = appLog.getType().getValue();
    l.subType = appLog.getSubType().getValue();
    l.requestIdSource = appLog.getParamAsString(AppLogParam.REQUEST_ID_SOURCE);
    l.className = appLog.getParamAsString(AppLogParam.CLASS_NAME);
    l.methodName = appLog.getParamAsString(AppLogParam.METHOD_NAME);
    l.httpMethod = appLog.getParamAsString(AppLogParam.HTTP_METHOD);
    l.path = appLog.getParamAsString(AppLogParam.PATH);
    l.queryParameters = appLog.getParamAsString(AppLogParam.QUERY_PARAMETERS);
    return l;
  }

  public Long getId() {
    return id;
  }
}
