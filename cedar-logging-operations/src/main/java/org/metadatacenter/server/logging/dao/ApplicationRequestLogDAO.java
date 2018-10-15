package org.metadatacenter.server.logging.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.metadatacenter.server.logging.dbmodel.ApplicationRequestLog;

public class ApplicationRequestLogDAO extends AbstractDAO<ApplicationRequestLog> {

  public ApplicationRequestLogDAO(SessionFactory factory) {
    super(factory);
  }

  public Long create(ApplicationRequestLog log) {
    return persist(log).getId();
  }

}