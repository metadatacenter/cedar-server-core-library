package org.metadatacenter.util;

import com.mongodb.MongoClient;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MongoFactory {

  @NonNull
  private static final MongoClient mongoClient = new MongoClient();

  @NonNull
  public static MongoClient getClient() {
    return mongoClient;
  }
}
