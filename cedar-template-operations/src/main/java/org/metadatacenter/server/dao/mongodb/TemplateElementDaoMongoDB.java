package org.metadatacenter.server.dao.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;

public class TemplateElementDaoMongoDB extends GenericLDDaoMongoDB {

  public TemplateElementDaoMongoDB(@NonNull String dbName, @NonNull String collectionName, String
      linkedDataIdBasePath) {
    super(dbName, collectionName, linkedDataIdBasePath);
  }
}
