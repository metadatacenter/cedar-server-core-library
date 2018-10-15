package org.metadatacenter.server.logging;

import io.dropwizard.hibernate.UnitOfWork;
import org.metadatacenter.server.logging.dao.ApplicationCypherLogDAO;
import org.metadatacenter.server.logging.dao.ApplicationRequestLogDAO;
import org.metadatacenter.server.logging.dbmodel.ApplicationCypherLog;
import org.metadatacenter.server.logging.dbmodel.ApplicationRequestLog;
import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.logging.model.AppLogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLoggerExecutorService {

  private static final Logger log = LoggerFactory.getLogger(AppLoggerExecutorService.class);
  private ApplicationRequestLogDAO requestLogDAO;
  private ApplicationCypherLogDAO cypherLogDAO;

  public AppLoggerExecutorService(ApplicationRequestLogDAO requestLogDAO,
                                  ApplicationCypherLogDAO cypherLogDAO) {
    this.requestLogDAO = requestLogDAO;
    this.cypherLogDAO = cypherLogDAO;
  }

  @UnitOfWork
  public void handleLog(AppLogMessage appLog) {
    if (appLog.getType() == AppLogType.REQUEST_FILTER) {
      ApplicationRequestLog l = new ApplicationRequestLog();
      l.setServerName(appLog.getServerName().getName());
      requestLogDAO.create(l);
    } else if (appLog.getType() == AppLogType.CYPHER_QUERY) {
      ApplicationCypherLog l = new ApplicationCypherLog();
      l.setServerName(appLog.getServerName().getName());
      cypherLogDAO.create(l);
    }
  }
}
