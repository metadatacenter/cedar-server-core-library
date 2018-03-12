package org.metadatacenter.server.search.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.constant.LinkedData;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ModelNodeNames;
import org.metadatacenter.model.ModelPaths;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.search.elasticsearch.document.field.CedarIndexFieldSchema;
import org.metadatacenter.server.search.elasticsearch.document.field.CedarIndexFieldValue;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.url.MicroserviceUrlUtil;
import org.metadatacenter.util.http.CedarEntityUtil;
import org.metadatacenter.util.http.CedarUrlUtil;
import org.metadatacenter.util.http.ProxyUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.metadatacenter.constant.CedarConstants.SCHEMA_IS_BASED_ON;

public class IndexUtils {

  protected static final Logger log = LoggerFactory.getLogger(IndexUtils.class);

  private final String FIELD_SUFFIX = "_field";
  private final String URI_LABEL_FIELD = "rdfs:label";

  private final int limit;
  private final int maxAttempts;
  private final int delayAttempts;
  private final MicroserviceUrlUtil microserviceUrlUtil;

  private enum ESType {
    STRING, LONG, INTEGER, SHORT, DOUBLE, FLOAT, DATE, BOOLEAN;

    public String toString() {
      return name().toLowerCase();
    }
  }

  public IndexUtils(CedarConfig cedarConfig) {
    microserviceUrlUtil = cedarConfig.getMicroserviceUrlUtil();
    this.limit = cedarConfig.getFolderRESTAPI().getPagination().getMaxPageSize();
    this.maxAttempts = cedarConfig.getSearchSettings().getSearchRetrieveSettings().getMaxAttempts();
    this.delayAttempts = cedarConfig.getSearchSettings().getSearchRetrieveSettings().getDelayAttempts();
  }

  /**
   * This method retrieves all the resources from the Workspace Server that are expected to be in the search index.
   * Those resources that don't have to be in the index, such as the "/" folder and the "Lost+Found" folder are ignored.
   */
  public List<FolderServerNode> findAllResources(CedarRequestContext context) throws CedarProcessingException {
    log.info("Retrieving all resources:");
    List<FolderServerNode> resources = new ArrayList<>();
    boolean finished = false;
    String baseUrl = microserviceUrlUtil.getWorkspace().getNodes();
    int offset = 0;
    int countSoFar = 0;
    while (!finished) {
      String url = baseUrl + "?offset=" + offset + "&limit=" + limit;
      log.info("Retrieving resources from Workspace Server. Url: " + url);
      int statusCode = -1;
      int attemp = 1;
      HttpResponse response = null;
      while (true) {
        response = ProxyUtil.proxyGet(url, context);
        statusCode = response.getStatusLine().getStatusCode();
        if ((statusCode != HttpStatus.SC_BAD_GATEWAY) || (attemp > maxAttempts)) {
          break;
        } else {
          log.error("Failed to retrieve resource. The Workspace Server might have not been started yet. " +
              "Retrying... (attemp " + attemp + "/" + maxAttempts + ")");
          attemp++;
          try {
            Thread.sleep(delayAttempts);
          } catch (InterruptedException e) {
            log.error("Error while waiting", e);
          }
        }
      }
      // The resources were successfully retrieved
      if (statusCode == HttpStatus.SC_OK) {
        JsonNode resultJson = null;
        try {
          resultJson = JsonMapper.MAPPER.readTree(CedarEntityUtil.toString(response.getEntity()));
        } catch (Exception e) {
          throw new CedarProcessingException(e);
        }
        int count = resultJson.get("resources").size();
        int totalCount = resultJson.get("totalCount").asInt();
        countSoFar += count;
        log.info("Retrieved " + countSoFar + "/" + totalCount + " resources");
        int currentOffset = resultJson.get("currentOffset").asInt();
        for (JsonNode resource : resultJson.get("resources")) {
          FolderServerNode folderServerNode = JsonMapper.MAPPER.convertValue(resource, FolderServerNode.class);
          if (needsIndexing(folderServerNode)) {
            resources.add(folderServerNode);
          } else {
            log.info("The node '" + resource.at(ModelPaths.SCHEMA_NAME).asText() + "' has been ignored");
          }
        }
        if (currentOffset + count >= totalCount) {
          finished = true;
        } else {
          offset = offset + count;
        }
      } else {
        throw new CedarProcessingException("Error retrieving resources from the Workspace server. HTTP status code: " +
            statusCode + " (" + response.getStatusLine().getReasonPhrase() + ")");
      }
    }
    return resources;
  }

  public boolean needsIndexing(FolderServerNode folderServerNode) {
    boolean needsIndexing = true;
    if (folderServerNode.getType() == CedarNodeType.FOLDER) {
      FolderServerFolder folderServerFolder = (FolderServerFolder) folderServerNode;
      if (folderServerFolder.isSystem()) {
        needsIndexing = false;
      } else if (folderServerFolder.isUserHome()) {
        needsIndexing = false;
      }
    }
    return needsIndexing;
  }

  /**
   * Returns the full content of a particular resource
   */
  public JsonNode findResourceContent(String resourceId, CedarNodeType nodeType, CedarRequestContext context) throws
      CedarProcessingException {
    try {
      CedarPermission permission = null;
      String resourceUrl = microserviceUrlUtil.getTemplate().getNodeType(nodeType);
      resourceUrl += "/" + CedarUrlUtil.urlEncode(resourceId);
      // Retrieve resource by id
      JsonNode resource = null;
      HttpResponse response = ProxyUtil.proxyGet(resourceUrl, context);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        String resourceString = EntityUtils.toString(response.getEntity());
        resource = JsonMapper.MAPPER.readTree(resourceString);
      } else {
        throw new CedarProcessingException("Error while retrieving resource content");
      }
      return resource;
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  // Returns summary of resourceContent. There is no need to index the full JSON for each resource. Only the
  // information necessary to satisfy search and value recommendation use cases is kept.
  public JsonNode extractSummarizedContent(CedarNodeType nodeType, JsonNode resourceContent, CedarRequestContext
      context) throws CedarProcessingException {
    try {
      // Templates and Elements
      if (nodeType.equals(CedarNodeType.TEMPLATE) || (nodeType.equals(CedarNodeType.ELEMENT))) {
        JsonNode schemaSummary = extractSchemaSummary(nodeType, resourceContent, JsonNodeFactory.instance.objectNode
            (), null);
        return schemaSummary;
      }
      // Instances
      else if (nodeType.equals(CedarNodeType.INSTANCE)) {
        // TODO: avoid calling this method multiple times when posting multiple instances for the same template
        JsonNode schemaSummary = extractSchemaSummary(nodeType, resourceContent, JsonNodeFactory.instance.objectNode
            (), context);
        JsonNode valuesSummary = extractValuesSummary(nodeType, schemaSummary, resourceContent, JsonNodeFactory
            .instance.objectNode());
        return valuesSummary;
      } else {
        throw new InternalError("Invalid node type: " + nodeType);
      }
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
  }

  private JsonNode extractSchemaSummary(CedarNodeType nodeType, JsonNode resourceContent, JsonNode results,
                                        CedarRequestContext context) throws CedarProcessingException {
    if (nodeType.compareTo(CedarNodeType.TEMPLATE) == 0 || nodeType.compareTo(CedarNodeType.ELEMENT) == 0) {

      Iterator<Map.Entry<String, JsonNode>> fieldsIterator = resourceContent.fields();
      while (fieldsIterator.hasNext()) {
        Map.Entry<String, JsonNode> field = fieldsIterator.next();
        final String fieldKey = field.getKey();
        if (field.getValue().isContainerNode()) {
          JsonNode fieldNode;
          // Single-instance fields
          if (field.getValue().has("items") == false) {
            fieldNode = field.getValue();
          }
          // Multi-instance fields
          else {
            fieldNode = field.getValue().get("items");
          }
          // Field
          if (fieldNode.get(LinkedData.TYPE) != null
              && fieldNode.get(LinkedData.TYPE).asText().equals(CedarNodeType.FIELD.getAtType())
              && fieldNode.get(ModelNodeNames.SCHEMA_NAME) != null) {
            if (!(fieldNode.get("_ui").get("inputType").asText().equals("attribute-value"))) {

              String fieldName = fieldNode.get(ModelNodeNames.SCHEMA_NAME).asText();
              String fieldType = getFieldType(fieldNode.get("_ui").get("inputType").asText());
              // Get field semantic type (if it has been defined)
              String fieldSemanticType = null;
              if ((fieldNode.get("properties").get(LinkedData.TYPE).get("oneOf") != null) && (fieldNode.get
                  ("properties").get(LinkedData.TYPE).get("oneOf").get(0).get("enum") != null)) {
                fieldSemanticType = fieldNode.get("properties").get(LinkedData.TYPE).get("oneOf").get(0).get("enum")
                    .get(0).asText();
              }
              CedarIndexFieldSchema f = new CedarIndexFieldSchema();
              f.setFieldName(fieldName);
              f.setFieldSemanticType(fieldSemanticType);
              f.setFieldValueType(fieldType);
              String outputFieldKey = fieldKey + FIELD_SUFFIX;
              // Add object to the results
              ((ObjectNode) results).set(outputFieldKey, JsonMapper.MAPPER.valueToTree(f));
            }
          } else {
            // Element
            if (fieldNode.get(LinkedData.TYPE) != null && fieldNode.get(LinkedData.TYPE).asText().equals
                (CedarNodeType.ELEMENT.getAtType())) {
              // Add empty object to the results
              ((ObjectNode) results).set(fieldKey, JsonNodeFactory.instance.objectNode());
              extractSchemaSummary(nodeType, fieldNode, results.get(fieldKey), context);
            }
            // Other nodes
            else {
              extractSchemaSummary(nodeType, fieldNode, results, context);
            }
          }
        }
      }
      // If the resource is an instance, the field names must be extracted from the template
    } else if (nodeType.compareTo(CedarNodeType.INSTANCE) == 0) {
      if (resourceContent.get(SCHEMA_IS_BASED_ON) != null) {
        String templateId = resourceContent.get(SCHEMA_IS_BASED_ON).asText();
        JsonNode templateJson = null;
        try {
          templateJson = findResourceContent(templateId, CedarNodeType.TEMPLATE, context);
          results = extractSchemaSummary(CedarNodeType.TEMPLATE, templateJson, results, context);
        } catch (CedarProcessingException e) {
          log.error("Error while accessing the reference template for the instance. It may have been removed");
          log.error("Instance id: " + resourceContent.get(LinkedData.ID));
          log.error("Template id: " + templateId);
        }
      }
    }
    return results;
  }

  // TODO: add remaining field types
  private String getFieldType(String inputType) {
    if (inputType.equals("textfield")) {
      return ESType.STRING.toString();
    } else {
      return ESType.STRING.toString();
    }
  }

  private JsonNode extractValuesSummary(CedarNodeType nodeType, JsonNode schemaSummary, JsonNode resourceContent,
                                        JsonNode results) throws JsonProcessingException {
    if (nodeType.compareTo(CedarNodeType.INSTANCE) == 0) {
      Iterator<Map.Entry<String, JsonNode>> fieldsIterator = resourceContent.fields();
      while (fieldsIterator.hasNext()) {
        Map.Entry<String, JsonNode> field = fieldsIterator.next();
        if (field.getValue().isContainerNode()) {
          if (!field.getKey().equals(LinkedData.CONTEXT)) {
            // Single value
            if (field.getValue().isObject()) {
              // If it is a Template Field (single instance)
              if (isTemplateField(field.getKey(), schemaSummary)) {

                JsonNode fieldSchema = null;
                if (schemaSummary != null && schemaSummary.has(field.getKey() + FIELD_SUFFIX)) {
                  fieldSchema = schemaSummary.get(field.getKey() + FIELD_SUFFIX);
                }
                if (fieldSchema != null) {
                  Optional<CedarIndexFieldValue> fv = valueToIndexValue(field.getValue(), fieldSchema);
                  if (fv.isPresent()) {
                    String outputFieldKey = field.getKey() + FIELD_SUFFIX;
                    ((ObjectNode) results).set(outputFieldKey, JsonMapper.MAPPER.valueToTree(fv.get()));
                  }
                }
                // It is a Template Element
              } else if (isTemplateElement(field.getKey(), schemaSummary)) {
                ((ObjectNode) results).set(field.getKey(), JsonNodeFactory.instance.objectNode());
                extractValuesSummary(nodeType, schemaSummary.get(field.getKey()), field.getValue(), results.get
                    (field.getKey()));
              }
            }
            // Array
            else if (field.getValue().isArray()) {
              ((ObjectNode) results).set(field.getKey(), JsonNodeFactory.instance.arrayNode());
              for (int i = 0; i < field.getValue().size(); i++) {
                JsonNode arrayItem = field.getValue().get(i);
                // It is a Template Field (multi-instance)
                if (isTemplateField(field.getKey(), schemaSummary)) {

                  String fieldValueName = getFieldValueName(arrayItem);

                  JsonNode fieldSchema = schemaSummary.get(field.getKey() + FIELD_SUFFIX);
                  // If the field was not found in the template, it is ignored. It may happen when template updated
                  if (fieldSchema != null) {
                    Optional<CedarIndexFieldValue> fv = valueToIndexValue(arrayItem, fieldSchema);
                    if (fv.isPresent()) {
                      ((ArrayNode) results.get(field.getKey())).add(JsonMapper.MAPPER.valueToTree(fv.get()));
                    }
                  }


                }
                // It is a Template Element (multi-instance)
                else if (isTemplateElement(field.getKey(), schemaSummary)) {
                  ((ArrayNode) results.get(field.getKey())).add(JsonNodeFactory.instance.objectNode());
                  extractValuesSummary(nodeType, schemaSummary.get(field.getKey()), arrayItem, results.get(field
                      .getKey()).get(i));
                }
              }
            }
          }
        }
      }
    }
    return results;
  }

  // Checks if JSON the field is a template field using information from the template
  private boolean isTemplateField(String fieldName, JsonNode schemaSummary) {
    String templateFieldName = fieldName + FIELD_SUFFIX;
    if (schemaSummary.has(templateFieldName)) {
      return true; // It is a template field
    } else {
      return false;
    }
  }

  // Checks if JSON the field is a template element using information from the template
  private boolean isTemplateElement(String fieldName, JsonNode schemaSummary) {
    if (schemaSummary.has(fieldName)) {
      return true; // It is a template element
    } else {
      return false;
    }
  }

  private boolean isControlledValue(JsonNode valueNode) {
    if (valueNode.has(URI_LABEL_FIELD)) {
      return true;
    } else {
      return false;
    }
  }

  private String getFieldValueName(JsonNode item) {
    if (item.has(LinkedData.VALUE)) {
      return LinkedData.VALUE;
    }
    return LinkedData.ID;
  }

  private Optional<CedarIndexFieldValue> valueToIndexValue(JsonNode valueNode, JsonNode fieldSchema) throws
      JsonProcessingException {
    CedarIndexFieldSchema fs = JsonMapper.MAPPER.treeToValue(fieldSchema, CedarIndexFieldSchema.class);
    CedarIndexFieldValue indexValue = null;

    String fieldValueName = getFieldValueName(valueNode);
    if (valueNode.has(fieldValueName)) {
      indexValue = new CedarIndexFieldValue();
      // If the field was not found in the template, it is ignored. It may happen if the template is updated
      if (!valueNode.isNull()) {
        indexValue.setFieldName(fs.getFieldName());
        // Free text value
        if (!isControlledValue(valueNode)) {
          // Set appropriate value field according to the value type
          if (fs.getFieldValueType().equals(ESType.STRING.toString())) {
            // Avoid indexing the empty string
            if (valueNode.get(fieldValueName).asText().trim().length() > 0) {
              indexValue.setFieldValue_string(valueNode.get(fieldValueName).asText());
            }
          }
          // TODO: add all remaining field types
          else {
            // Avoid indexing the empty string
            if (valueNode.asText().trim().length() > 0) {
              indexValue.setFieldValue_string(valueNode.get(fieldValueName).asText());
            }
          }
        }
        // Controlled value
        else {
          // Controlled term preferred name
          JsonNode valueLabelNode = valueNode.get(URI_LABEL_FIELD);
          indexValue.setFieldValue_string(valueLabelNode.asText());
          // Term URI
          indexValue.setFieldValueSemanticType(valueNode.get(fieldValueName).asText());
          indexValue.setFieldValueAndSemanticType(indexValue.generateFieldValueAndSemanticType());
        }
      } else {
        // Do nothing. Null values will not be indexed
      }
    }
    return Optional.ofNullable(indexValue);
  }

  public String getNewIndexName(String prefix) {
    Instant now = Instant.now();
    String dateTimeFormatterString = "uuuu-MM-dd't'HH:mm:ss";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterString).withZone(ZoneId
        .systemDefault());
    String nowString = dateTimeFormatter.format(now);
    return prefix + "-" + nowString;
  }


}