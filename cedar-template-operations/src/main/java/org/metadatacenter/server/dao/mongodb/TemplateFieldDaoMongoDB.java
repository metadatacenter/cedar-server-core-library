package org.metadatacenter.server.dao.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;

public class TemplateFieldDaoMongoDB extends GenericLDDaoMongoDB {

  public TemplateFieldDaoMongoDB(@NonNull String dbName, @NonNull String collectionName, String
      linkedDataIdBasePath) {
    super(dbName, collectionName, linkedDataIdBasePath);
  }
}
