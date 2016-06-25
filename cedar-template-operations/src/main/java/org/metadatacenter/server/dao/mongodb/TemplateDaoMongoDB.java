package org.metadatacenter.server.dao.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;

public class TemplateDaoMongoDB extends GenericLDDaoMongoDB {

  public TemplateDaoMongoDB(@NonNull String dbName, @NonNull String collectionName) {
    super(dbName, collectionName);
  }

}