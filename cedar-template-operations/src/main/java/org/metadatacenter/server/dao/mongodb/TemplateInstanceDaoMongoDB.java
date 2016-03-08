package org.metadatacenter.server.dao.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;

public class TemplateInstanceDaoMongoDB extends GenericLDDaoMongoDB {

  public TemplateInstanceDaoMongoDB(@NonNull String dbName, @NonNull String collectionName, String
      linkedDataIdBasePath) {
    super(dbName, collectionName, linkedDataIdBasePath);
  }
}
