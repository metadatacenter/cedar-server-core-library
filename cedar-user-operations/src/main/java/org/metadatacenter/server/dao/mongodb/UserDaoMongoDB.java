package org.metadatacenter.server.dao.mongodb;

import checkers.nullness.quals.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.metadatacenter.util.FixMongoDirection;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class UserDaoMongoDB extends GenericDaoMongoDB {

  public UserDaoMongoDB(@NonNull String dbName, @NonNull String collectionName) {
    super(dbName, collectionName);
  }

  public JsonNode findByApiKey(String apiKey) throws IOException {
    if ((apiKey == null) || (apiKey.length() == 0)) {
      throw new IllegalArgumentException();
    }


    Document doc = entityCollection.find(eq("apiKeys", apiKey)).first();
    if (doc == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return jsonUtils.fixMongoDB(mapper.readTree(doc.toJson()), FixMongoDirection.READ_FROM_MONGO);
  }
}