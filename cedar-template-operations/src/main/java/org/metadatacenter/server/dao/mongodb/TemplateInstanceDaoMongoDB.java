package org.metadatacenter.server.dao.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import org.checkerframework.checker.nullness.qual.NonNull;

import static org.metadatacenter.constant.CedarConstants.SCHEMA_IS_BASED_ON;

public class TemplateInstanceDaoMongoDB extends GenericLDDaoMongoDB {

  public TemplateInstanceDaoMongoDB(@NonNull MongoClient mongoClient, @NonNull String dbName, @NonNull String
      collectionName) {
    super(mongoClient, dbName, collectionName);
  }

  public long countReferencingTemplate(@NonNull String templateId) {
    return entityCollection.count(Filters.eq(SCHEMA_IS_BASED_ON, templateId));
  }
}
