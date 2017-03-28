package org.metadatacenter.server.dao.mongodb;

import com.mongodb.MongoClient;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TemplateDaoMongoDB extends GenericLDDaoMongoDB {

  public TemplateDaoMongoDB(@NonNull MongoClient mongoClient, @NonNull String dbName, @NonNull String collectionName) {
    super(mongoClient, dbName, collectionName);
  }

}