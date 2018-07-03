package org.metadatacenter.server.dao.mongodb;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.server.dao.GenericUserDao;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserUIFolderView;
import org.metadatacenter.server.security.model.user.CedarUserUIPreferences;
import org.metadatacenter.util.json.JsonMapper;
import org.metadatacenter.util.json.JsonUtils;
import org.metadatacenter.util.mongo.FixMongoDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class UserDaoMongoDB implements GenericUserDao {

  protected final
  MongoCollection<Document> entityCollection;
  protected final
  JsonUtils jsonUtils;
  protected final static String USER_PK_FIELD = "@id";

  public UserDaoMongoDB(MongoClient mongoClient, String dbName, String collectionName) {
    entityCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
    jsonUtils = new JsonUtils();
  }

  @Override
  public CedarUser create(CedarUser user) throws IOException {
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
  public CedarUser find(String id) throws IOException {
    if ((id == null) || (id.length() == 0)) {
      throw new IllegalArgumentException();
    }
    Document doc = entityCollection.find(eq(USER_PK_FIELD, id)).first();
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

  public boolean exists(String id) throws IOException {
    return (find(id) != null);
  }

  @Override
  public BackendCallResult<CedarUser> patch(String id, JsonNode modifications) {
    BackendCallResult<CedarUser> result = new BackendCallResult();
    if ((id == null) || (id.length() == 0)) {
      result.addError(CedarErrorType.INVALID_ARGUMENT)
          .message("The id empty")
          .parameter("id", id);
      return result;
    }
    try {
      if (!exists(id)) {
        result.addError(CedarErrorType.NOT_FOUND)
            .message("The user can not be found by id")
            .parameter("id", id);
        return result;
      }
      CedarUser cedarUser = find(id);
      // Adapts all keys not accepted by MongoDB
      modifications = jsonUtils.fixMongoDB(modifications, FixMongoDirection.WRITE_TO_MONGO);
      Map modificationsMap = JsonMapper.MAPPER.convertValue(modifications, Map.class);
      boolean modificationsOk = validateModifications(cedarUser, modificationsMap);
      if (modificationsOk) {
        UpdateResult updateResult = entityCollection.updateOne(eq(USER_PK_FIELD, id),
            new Document("$set", modificationsMap));
        long matchedCount = updateResult.getMatchedCount();
        if (matchedCount == 1) {
          result.setPayload(find(id));
          return result;
        } else {
          result.addError(CedarErrorType.SERVER_ERROR)
              .message("There was an error while patching the user")
              .parameter("id", id)
              .parameter("matchedCount", matchedCount);
          return result;
        }
      } else {
        result.addError(CedarErrorType.INVALID_ARGUMENT)
            .message("The requested modifications are invalid")
            .parameter("id", id)
            .parameter("modifications", modifications);
        return result;
      }
    } catch (IOException e) {
      result.addError(CedarErrorType.SERVER_ERROR)
          .message("There was an error while patching the user")
          .parameter("id", id)
          .sourceException(e);
      return result;
    }
  }

  @Override
  public BackendCallResult<CedarUser> update(String id, CedarUser user) {
    BackendCallResult<CedarUser> result = new BackendCallResult();
    if ((id == null) || (id.length() == 0)) {
      result.addError(CedarErrorType.INVALID_ARGUMENT)
          .message("The id empty")
          .parameter("id", id);
      return result;
    }
    try {
      if (!exists(id)) {
        result.addError(CedarErrorType.NOT_FOUND)
            .message("The user can not be found by id")
            .parameter("id", id);
        return result;
      }
      CedarUser cedarUser = find(id);
      // Adapts all keys not accepted by MongoDB
      JsonNode userNode = JsonMapper.MAPPER.valueToTree(user);
      userNode = jsonUtils.fixMongoDB(userNode, FixMongoDirection.WRITE_TO_MONGO);
      Map userMap = JsonMapper.MAPPER.convertValue(userNode, Map.class);
      UpdateResult updateResult = entityCollection.updateOne(eq(USER_PK_FIELD, id),
          new Document("$set", userMap));
      long matchedCount = updateResult.getMatchedCount();
      if (matchedCount == 1) {
        result.setPayload(find(id));
        return result;
      } else {
        result.addError(CedarErrorType.SERVER_ERROR)
            .message("There was an error while updating the user")
            .parameter("id", id)
            .parameter("matchedCount", matchedCount);
        return result;
      }
    } catch (IOException e) {
      result.addError(CedarErrorType.SERVER_ERROR)
          .message("There was an error while updating the user")
          .parameter("id", id)
          .sourceException(e);
      return result;
    }
  }

  private boolean validateModifications(CedarUser cedarUser, Map<String, Object> modificationsMap) {
    JsonNode userNode = JsonMapper.MAPPER.valueToTree(cedarUser);
    for (String k : modificationsMap.keySet()) {
      String pointerS = "/" + k.replace(".", "/");
      pointerS.replaceAll("//", "/");
      if (!pointerS.startsWith("/uiPreferences/")) {
        return false;
      }
      JsonPointer pointer = JsonPointer.compile(pointerS);
      JsonNode v = userNode.at(pointer);
      if (!v.isMissingNode()) {
        JsonNode newValue = JsonMapper.MAPPER.valueToTree(modificationsMap.get(k));
        JsonNode parentNode = userNode.at(pointer.head());
        String lastNodeName = pointer.last().toString().replace("/", "");
        ((ObjectNode) parentNode).set(lastNodeName, newValue);
      } else {
        return false;
      }
    }
    CedarUser modifiedUser = null;
    try {
      modifiedUser = JsonMapper.MAPPER.convertValue(userNode, CedarUser.class);
      if (!userUIPreferencesAreNotNull(modifiedUser)) {
        return false;
      }
    } catch (Exception e) {
      // The
      //DO nothing, it means the modifications render the user invalid.
    }
    return modifiedUser != null;
  }

  // TODO: write a method which check the NonNull fields with reflection
  private boolean userUIPreferencesAreNotNull(CedarUser user) {
    CedarUserUIPreferences uiPreferences = user.getUiPreferences();
    if (uiPreferences == null) {
      return false;
    }
    if (uiPreferences.getStylesheet() == null) {
      return false;
    }
    if (uiPreferences.getTemplateEditor() == null) {
      return false;
    }
    if (uiPreferences.getMetadataEditor() == null) {
      return false;
    }
    if (uiPreferences.getInfoPanel() == null) {
      return false;
    }
    if (uiPreferences.getResourceTypeFilters() == null) {
      return false;
    }
    if (uiPreferences.getResourceVersionFilter() == null) {
      return false;
    }
    if (uiPreferences.getResourcePublicationStatusFilter() == null) {
      return false;
    }
    if (uiPreferences.getFolderView() == null) {
      return false;
    } else {
      CedarUserUIFolderView folderView = uiPreferences.getFolderView();
      if (folderView.getSortBy() == null || folderView.getSortDirection() == null || folderView.getViewMode() == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public List<CedarUser> findAll() throws IOException {
    FindIterable<Document> findIterable = entityCollection.find();
    MongoCursor<Document> cursor = findIterable.iterator();
    List<CedarUser> users = new ArrayList<>();
    try {
      while (cursor.hasNext()) {
        JsonNode readUser = JsonMapper.MAPPER.readTree(cursor.next().toJson());
        JsonNode fixedUser = jsonUtils.fixMongoDB(readUser, FixMongoDirection.READ_FROM_MONGO);
        users.add(JsonMapper.MAPPER.treeToValue(fixedUser, CedarUser.class));
      }
    } finally {
      cursor.close();
    }
    return users;
  }


}