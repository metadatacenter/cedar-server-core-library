package org.metadatacenter.server.logging;

import org.metadatacenter.model.ServerName;
import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.logging.model.AppLogSubType;
import org.metadatacenter.server.logging.model.AppLogType;

public class AppLogger {

  public static AppLoggerQueueService appLoggerQueueService;
  private static ServerName serverName;

  public static void initLoggerQueueService(AppLoggerQueueService appLoggerQueueService, ServerName serverName) {
    AppLogger.appLoggerQueueService = appLoggerQueueService;
    AppLogger.serverName = serverName;
  }

  public static AppLogMessage message(AppLogType type, AppLogSubType subType, String requestId) {
    AppLogMessage m = new AppLogMessage(serverName, requestId, type, subType);
    return m;
  }

  public static void enqueue(AppLogMessage appLogMessage) {
    appLoggerQueueService.enqueueEvent(appLogMessage);
  }

}
