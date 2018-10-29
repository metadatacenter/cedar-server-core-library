package org.metadatacenter.server.search.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ModelPaths;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.search.elasticsearch.service.ElasticsearchManagementService;
import org.metadatacenter.server.url.MicroserviceUrlUtil;
import org.metadatacenter.util.http.CedarEntityUtil;
import org.metadatacenter.util.http.ProxyUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IndexUtils {

  protected static final Logger log = LoggerFactory.getLogger(IndexUtils.class);

  private final int limit;
  private final int maxAttempts;
  private final int delayAttempts;
  private final MicroserviceUrlUtil microserviceUrlUtil;

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
    log.info("Retrieving all resources.");
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
      }
    }
    return needsIndexing;
  }

  public String getNewIndexName(String prefix) {
    Instant now = Instant.now();
    String dateTimeFormatterString = "uuuu-MM-dd't'HH:mm:ss";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterString).withZone(ZoneId
        .systemDefault());
    String nowString = dateTimeFormatter.format(now);
    return prefix + "-" + nowString;
  }


  public void deleteOldIndices(ElasticsearchManagementService esManagementService, String aliasName,
                               String newIndexName) throws CedarProcessingException {
    log.info("Listing existing indices.");
    List<String> indexNames = esManagementService.getAllIndices();
    log.info("Found " + indexNames.size());
    for (String iName : indexNames) {
      log.info("Found index:" + iName);
      if (iName.startsWith(aliasName)) {
        if (!iName.equals(newIndexName)) {
          log.info("Deleting existing index:" + iName);
          esManagementService.deleteIndex(iName);
        } else {
          log.info("Keeping existing index, since it was just generated:" + iName);
        }
      } else {
        log.info("Not touching existing index, it is not in the scope of this task:" + iName);
      }
    }
  }

  public void ensureIndexAndAliasExist(ElasticsearchManagementService esManagementService, String aliasName)
      throws CedarProcessingException {
    int cedarIndexCount = 0;

    log.info("Looking for existing CEDAR indices...");
    List<String> indexNames = esManagementService.getAllIndices();
    log.info("Found total of " + indexNames.size() + " indices");
    for (String iName : indexNames) {
      log.info("Looking at index:" + iName);
      if (iName.startsWith(aliasName)) {
        log.info("Found CEDAR index:" + iName);
        cedarIndexCount++;
      }
    }
    log.info("Found total of " + cedarIndexCount + " CEDAR indices");
    if (cedarIndexCount > 0) {
      log.info("Nothing to do!");
    } else {
      String newIndexName = getNewIndexName(aliasName);
      log.info("Creating brand new CEDAR index:" + newIndexName);
      esManagementService.createSearchIndex(newIndexName);
      esManagementService.addAlias(newIndexName, aliasName);
    }

  }
}