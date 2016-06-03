package org.metadatacenter.server.dao.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.dao.GenericUserDao;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.util.FixMongoDirection;
import org.metadatacenter.util.MongoFactory;
import org.metadatacenter.util.json.JsonMapper;
import org.metadatacenter.util.json.JsonUtils;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class UserDaoMongoDB implements GenericUserDao {

  protected final @NonNull MongoCollection<Document> entityCollection;
  protected final @NonNull JsonUtils jsonUtils;

  public UserDaoMongoDB(@NonNull String dbName, @NonNull String collectionName) {
    MongoClient mongoClient = MongoFactory.getClient();
    entityCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
    jsonUtils = new JsonUtils();
  }

  @Override
  @NonNull
  public CedarUser create(@NonNull CedarUser user) throws IOException {
    JsonNode userNode = JsonMapper.MAPPER.valueToTree(user);
    // Adapts all keys not accepted by MongoDB
    JsonNode fixedElement = jsonUtils.fixMongoDB(userNode, FixMongoDirection.WRITE_TO_MONGO);
    Map elementMap = JsonMapper.MAPPER.convertValue(fixedElement, Map.class);
    Document elementDoc = new Document(elementMap);
    entityCollection.insertOne(elementDoc);
    // Returns the document created (all keys adapted for MongoDB are restored)
    JsonNode savedUser = JsonMapper.MAPPER.readTree(elementDoc.toJson());
    JsonNode fixedUser = jsonUtils.fixMongoDB(savedUser, FixMongoDirection.READ_FROM_MONGO);
    return JsonMapper.MAPPER.treeToValue(fixedUser, CedarUser.class);
  }

  @Override
  public CedarUser find(@NonNull String id) throws IOException {
    if ((id == null) || (id.length() == 0)) {
      throw new IllegalArgumentException();
    }
    Document doc = entityCollection.find(eq("userId", id)).first();
    if (doc == null) {
      return null;
    }
    JsonNode readUser = JsonMapper.MAPPER.readTree(doc.toJson());
    JsonNode fixedUser = jsonUtils.fixMongoDB(readUser, FixMongoDirection.READ_FROM_MONGO);
    return JsonMapper.MAPPER.treeToValue(fixedUser, CedarUser.class);
  }

  public CedarUser findByApiKey(String apiKey) throws IOException {
    if ((apiKey == null) || (apiKey.length() == 0)) {
      throw new IllegalArgumentException();
    }

    Document doc = entityCollection.find(eq("apiKeys.key", apiKey)).first();
    if (doc == null) {
      return null;
    }
    JsonNode readUser = JsonMapper.MAPPER.readTree(doc.toJson());
    JsonNode fixedUser = jsonUtils.fixMongoDB(readUser, FixMongoDirection.READ_FROM_MONGO);
    return JsonMapper.MAPPER.treeToValue(fixedUser, CedarUser.class);
  }

  public boolean exists(@NonNull String id) throws IOException {
    return (find(id) != null);
  }

  @Override
  public @NonNull CedarUser update(@NonNull String id, JsonNode modifications) throws IOException,
      InstanceNotFoundException {
    if ((id == null) || (id.length() == 0)) {
      throw new IllegalArgumentException();
    }
    if (!exists(id)) {
      throw new InstanceNotFoundException();
    }
    // Adapts all keys not accepted by MongoDB
    modifications = jsonUtils.fixMongoDB(modifications, FixMongoDirection.WRITE_TO_MONGO);
    Map modificationsMap = JsonMapper.MAPPER.convertValue(modifications, Map.class);
    UpdateResult updateResult = entityCollection.updateOne(eq("userId", id), new Document("$set", modificationsMap));
    if (updateResult.getMatchedCount() == 1) {
      return find(id);
    } else {
      throw new InternalError();
    }
  }


}