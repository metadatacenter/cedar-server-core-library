package org.metadatacenter.server.logging.dbmodel;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.logging.model.AppLogParam;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "log_cypher")
public class ApplicationCypherLog {

  private static final Logger log = LoggerFactory.getLogger(ApplicationCypherLog.class);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 36)
  private String globalRequestId;

  @Column(length = 36)
  private String localRequestId;

  @Column(length = 50)
  private String systemComponentName;

  private long duration;

  private Instant logTime;

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

  @Column(length = 32)
  private String runnableHash;

  @Column(length = 32)
  private String parametersHash;

  public static ApplicationCypherLog fromAppCypherLog(AppLogMessage appLog) {
    ApplicationCypherLog l = new ApplicationCypherLog();
    l.globalRequestId = appLog.getGlobalRequestId();
    l.localRequestId = appLog.getLocalRequestId();
    l.systemComponentName = appLog.getSystemComponent().getStringValue();
    l.original = appLog.getParamAsString(AppLogParam.ORIGINAL_QUERY);
    l.runnable = appLog.getParamAsString(AppLogParam.RUNNABLE_QUERY);
    l.interpolated = appLog.getParamAsString(AppLogParam.INTERPOLATED_QUERY);
    l.runnableHash = appLog.getParamAsString(AppLogParam.RUNNABLE_QUERY_HASH);
    l.parametersHash = appLog.getParamAsString(AppLogParam.QUERY_PARAMETERS_HASH);
    l.logTime = appLog.getLogTime();
    l.duration = appLog.getDuration().toNanos();
    l.startTime = appLog.getStartTime();
    l.endTime = appLog.getEndTime();
    l.className = appLog.getParamAsString(AppLogParam.CLASS_NAME);
    l.methodName = appLog.getParamAsString(AppLogParam.METHOD_NAME);
    l.lineNumber = appLog.getParamAsInt(AppLogParam.LINE_NUMBER);
    l.operation = appLog.getParamAsString(AppLogParam.OPERATION);
    Map<String, Object> parameters = appLog.getParamAsMap(AppLogParam.QUERY_PARAMETERS);
    try {
      l.parameters = JsonMapper.PRETTY_MAPPER.writeValueAsString(parameters);
    } catch (JsonProcessingException e) {
      log.error("Error serializing parameters", e);
    }
    return l;
  }

  public Long getId() {
    return id;
  }
}
