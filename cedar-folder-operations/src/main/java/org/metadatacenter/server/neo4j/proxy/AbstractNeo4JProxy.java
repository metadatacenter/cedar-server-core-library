package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.constant.HttpConnectionConstants;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.metadatacenter.constant.HttpConstants.*;

public abstract class AbstractNeo4JProxy {

  protected final Neo4JProxies proxies;

  protected static final Logger log = LoggerFactory.getLogger(AbstractNeo4JProxy.class);

  protected AbstractNeo4JProxy(Neo4JProxies proxies) {
    this.proxies = proxies;
  }

  protected JsonNode executeCypherQueryAndCommit(CypherQuery query) {
    return executeCypherQueriesAndCommit(Collections.singletonList(query));
  }

  protected JsonNode executeCypherQueriesAndCommit(List<CypherQuery> queries) {
    List<Map<String, Object>> statements = new ArrayList<>();
    for (CypherQuery q : queries) {
      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        log.debug("c.query : " + qp.getFlatQuery());
        log.debug("c.params: " + qp.getParameters());
        log.debug("c.interp: " + qp.getLiteralCypher());
        Map<String, Object> statement = new HashMap<>();
        statement.put("statement", qp.getQuery());
        statement.put("parameters", qp.getParameters());
        statements.add(statement);
      } else if (q instanceof CypherQueryLiteral) {
        log.debug("c.string: " + q.getFlatQuery());
        CypherQueryLiteral qp = (CypherQueryLiteral) q;
        Map<String, Object> statement = new HashMap<>();
        statement.put("statement", qp.getQuery());
        statements.add(statement);
      }
    }

    Map<String, Object> body = new HashMap<>();
    body.put("statements", statements);

    String requestBody;
    try {
      requestBody = JsonMapper.MAPPER.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      log.error("Error serializing cypher queries", e);
      return null;
    }

    try {
      HttpResponse response = Request.Post(proxies.config.getTransactionUrl())
          .addHeader(HTTP_HEADER_AUTHORIZATION, proxies.config.getAuthString())
          .addHeader(HTTP_HEADER_ACCEPT, CONTENT_TYPE_APPLICATION_JSON)
          .addHeader("X-stream", "true")
          .bodyString(requestBody, ContentType.APPLICATION_JSON)
          .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
          .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
          .execute()
          .returnResponse();

      int statusCode = response.getStatusLine().getStatusCode();
      String responseAsString = EntityUtils.toString(response.getEntity());
      // TODO: Use a constant here: HTTP_OK
      if (statusCode == 200) {
        return JsonMapper.MAPPER.readTree(responseAsString);
      } else {
        return null;
      }

    } catch (IOException ex) {
      log.error("Error while reading user details from Keycloak", ex);
    }
    return null;
  }

  protected FolderServerGroup buildGroup(JsonNode g) {
    FolderServerGroup cg = null;
    if (g != null && !g.isMissingNode()) {
      try {
        cg = JsonMapper.MAPPER.treeToValue(g, FolderServerGroup.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing group", e);
      }
    }
    return cg;
  }

  protected FolderServerUser buildUser(JsonNode u) {
    FolderServerUser cu = null;
    if (u != null && !u.isMissingNode()) {
      try {
        cu = JsonMapper.MAPPER.treeToValue(u, FolderServerUser.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing user", e);
      }
    }
    return cu;
  }

  protected FolderServerFolder buildFolder(JsonNode f) {
    FolderServerFolder cf = null;
    if (f != null && !f.isMissingNode()) {
      try {
        cf = JsonMapper.MAPPER.treeToValue(f, FolderServerFolder.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing folder", e);
      }
    }
    return cf;
  }

  public FolderServerResource buildResource(JsonNode r) {
    FolderServerResource cr = null;
    if (r != null && !r.isMissingNode()) {
      try {
        cr = JsonMapper.MAPPER.treeToValue(r, FolderServerResource.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing resource", e);
      }
    }
    return cr;
  }

  protected FolderServerNode buildNode(JsonNode f) {
    FolderServerNode cf = null;
    if (f != null && !f.isMissingNode()) {
      try {
        cf = JsonMapper.MAPPER.treeToValue(f, FolderServerNode.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing node", e);
      }
    }
    return cf;
  }

  protected Map buildMap(JsonNode f) {
    Map map = null;
    if (f != null && !f.isMissingNode()) {
      try {
        map = JsonMapper.MAPPER.treeToValue(f, Map.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing map", e);
      }
    }
    return map;
  }

  protected List<FolderServerNode> listNodes(JsonNode jsonNode) {
    List<FolderServerNode> nodeList = new ArrayList<>();
    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        FolderServerNode cf = buildNode(nodeNode);
        if (cf != null) {
          nodeList.add(cf);
        }
      });
    }
    return nodeList;
  }

  protected List<FolderServerGroup> listGroups(JsonNode jsonNode) {
    List<FolderServerGroup> groupList = new ArrayList<>();
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode groupNode = f.at("/row/0");
        if (groupNode != null && !groupNode.isMissingNode()) {
          FolderServerGroup cg = buildGroup(groupNode);
          groupList.add(cg);
        }
      });
    }
    return groupList;
  }

  protected List<FolderServerUser> listUsers(JsonNode jsonNode) {
    List<FolderServerUser> userList = new ArrayList<>();
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          FolderServerUser cu = buildUser(userNode);
          userList.add(cu);
        }
      });
    }
    return userList;
  }


  /*
  String getFolderUUID(String folderId) {
    if (folderId != null && folderId.startsWith(folderIdPrefix)) {
      return folderId.substring(folderIdPrefix.length());
    } else {
      return null;
    }
  }
*/

}
