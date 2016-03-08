package org.metadatacenter.server.dao.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.metadatacenter.server.dao.GenericUserDao;
import org.metadatacenter.util.FixMongoDirection;
import org.metadatacenter.util.MongoFactory;
import org.metadatacenter.util.json.JsonUtils;

import java.io.IOException;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class GenericDaoMongoDB implements GenericUserDao<String, JsonNode> {

  @NonNull
  protected final MongoCollection<Document> entityCollection;
  protected final @NonNull JsonUtils jsonUtils;

  public GenericDaoMongoDB(@NonNull String dbName, @NonNull String collectionName) {
    MongoClient mongoClient = MongoFactory.getClient();
    entityCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
    jsonUtils = new JsonUtils();
  }
  @Override
  @NonNull
  public JsonNode create(@NonNull JsonNode element) throws IOException {
    // Adapts all keys not accepted by MongoDB
    JsonNode fixedElement = jsonUtils.fixMongoDB(element, FixMongoDirection.WRITE_TO_MONGO);
    ObjectMapper mapper = new ObjectMapper();
    Map elementMap = mapper.convertValue(fixedElement, Map.class);
    Document elementDoc = new Document(elementMap);
    entityCollection.insertOne(elementDoc);
    // Returns the document created (all keys adapted for MongoDB are restored)
    return jsonUtils.fixMongoDB(mapper.readTree(elementDoc.toJson()), FixMongoDirection.READ_FROM_MONGO);
  }

  @Override
  public JsonNode find(@NonNull String id) throws IOException {
    if ((id == null) || (id.length() == 0)) {
      throw new IllegalArgumentException();
    }
    Document doc = entityCollection.find(eq("@id", id)).first();
    if (doc == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return jsonUtils.fixMongoDB(mapper.readTree(doc.toJson()), FixMongoDirection.READ_FROM_MONGO);
  }

}
