package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.constant.HttpConnectionConstants;
import org.metadatacenter.model.CedarNode;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.util.json.JsonMapper;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.types.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.metadatacenter.constant.HttpConstants.*;

public abstract class AbstractNeo4JProxy {

  protected final Neo4JProxies proxies;

  protected final Driver driver;

  protected static final Logger log = LoggerFactory.getLogger(AbstractNeo4JProxy.class);

  protected AbstractNeo4JProxy(Neo4JProxies proxies) {
    this.proxies = proxies;
    driver = GraphDatabase.driver(proxies.config.getUri(),
        AuthTokens.basic(proxies.config.getUserName(), proxies.config.getUserPassword()));
  }

  protected boolean executeWrite(CypherQuery q, String eventDescription) {
    boolean result = false;
    try (Session session = driver.session()) {

      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        final String runnableQuery = qp.getRunnableQuery();
        final Map<String, Object> parameterMap = qp.getParameterMap();
        result = session.writeTransaction(tx -> {
          tx.run(runnableQuery, parameterMap);
          return true;
        });
      } else if (q instanceof CypherQueryLiteral) {
        final String runnableQuery = q.getRunnableQuery();
        result = session.writeTransaction(tx -> {
          tx.run(runnableQuery);
          return true;
        });
      }
    } catch (ClientException ex) {
      log.error("Error while " + eventDescription, ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }
    return result;
  }

  protected <T extends CedarNode> T executeWriteGetOne(CypherQuery q, Class<T> type) {
    Record record = null;
    try (Session session = driver.session()) {

      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        final String runnableQuery = qp.getRunnableQuery();
        final Map<String, Object> parameterMap = qp.getParameterMap();
        record = session.writeTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery, parameterMap);
          return result.hasNext() ? result.next() : null;
        });
      } else if (q instanceof CypherQueryLiteral) {
        final String runnableQuery = q.getRunnableQuery();
        record = session.writeTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery);
          return result.hasNext() ? result.next() : null;
        });
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }

    if (record != null) {
      Node n = record.get(0).asNode();
      if (n != null) {
        JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
        return buildClass(node, type);
      }
    }
    return null;
  }

  protected <T extends CedarNode> T executeReadGetOne(CypherQuery q, Class<T> type) {
    Record record = null;
    try (Session session = driver.session()) {

      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        final String runnableQuery = qp.getRunnableQuery();
        final Map<String, Object> parameterMap = qp.getParameterMap();
        record = session.readTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery, parameterMap);
          return result.hasNext() ? result.next() : null;
        });
      } else if (q instanceof CypherQueryLiteral) {
        final String runnableQuery = q.getRunnableQuery();
        record = session.readTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery);
          return result.hasNext() ? result.next() : null;
        });
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }

    if (record != null) {
      Node n = record.get(0).asNode();
      if (n != null) {
        JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
        return buildClass(node, type);
      }
    }
    return null;
  }


  protected <T extends CedarNode> List<T> executeReadGetList(CypherQuery q, Class<T> type) {
    List<T> folderServerNodeList = new ArrayList<>();
    List<Record> records = null;
    try (Session session = driver.session()) {
      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        final String runnableQuery = qp.getRunnableQuery();
        final Map<String, Object> parameterMap = qp.getParameterMap();
        records = session.readTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery, parameterMap);
          List<Record> nodes = new ArrayList<>();
          while (result.hasNext()) {
            nodes.add(result.next());
          }
          return nodes;
        });
      } else if (q instanceof CypherQueryLiteral) {
        final String runnableQuery = q.getRunnableQuery();
        records = session.readTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery);
          List<Record> nodes = new ArrayList<>();
          while (result.hasNext()) {
            nodes.add(result.next());
          }
          return nodes;
        });
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }

    if (records != null) {
      for (Record r : records) {
        Node n = r.get(0).asNode();
        if (n != null) {
          JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
          T folderServerNode = buildClass(node, type);
          folderServerNodeList.add(folderServerNode);
        }
      }

      return folderServerNodeList;
    }
    return null;
  }

  private <T extends CedarNode> T buildClass(JsonNode node, Class<T> type) {
    if (type == FolderServerUser.class) {
      return (T) buildUser(node);
    }
    return null;
  }


  protected JsonNode executeCypherQueryAndCommit(CypherQuery query) {
    return executeCypherQueriesAndCommit(Collections.singletonList(query));
  }

  protected JsonNode executeCypherQueriesAndCommit(List<CypherQuery> queries) {
    List<Map<String, Object>> statements = new ArrayList<>();
    for (CypherQuery q : queries) {
      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        String runnableQuery = qp.getRunnableQuery();
        Map<String, Object> parameterMap = qp.getParameterMap();
        log.debug("VVV----------------------------------------------------------------");
        log.debug("c.original     : " + qp.getOriginalQuery());
        log.debug("c.runnable     : " + runnableQuery);
        log.debug("c.parameters   : " + parameterMap);
        log.debug("c.interpolated : " + qp.getInterpolatedParamsQuery());
        log.debug("^^^----------------------------------------------------------------");
        Map<String, Object> statement = new HashMap<>();
        statement.put("statement", runnableQuery);
        statement.put("parameters", parameterMap);
        statements.add(statement);
      } else if (q instanceof CypherQueryLiteral) {
        String runnableQuery = q.getRunnableQuery();
        log.debug("c.original     : " + q.getOriginalQuery());
        log.debug("c.runnable     : " + runnableQuery);
        Map<String, Object> statement = new HashMap<>();
        statement.put("statement", runnableQuery);
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
      JsonNode cypherQueryResponse = null;
      boolean resultOk = false;
      if (responseAsString != null) {
        cypherQueryResponse = JsonMapper.MAPPER.readTree(responseAsString);
      } else {
        log.error("Error while reading cypher query response!");
      }
      if (cypherQueryResponse != null) {
        resultOk = successOrLogAndThrowException(cypherQueryResponse, "Error while executing cypher query:");
      }
      if (resultOk) {
        return cypherQueryResponse;
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

  // TODO: Handle the errors differently, propagate the error to an upper layer, or throw a checked exception
  protected boolean successOrLogAndThrowException(JsonNode jsonNode, String errorMessage) {
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      String code = null;
      String message = null;
      JsonNode error = errorsNode.path(0);
      log.error(errorMessage);
      JsonNode codeNode = error.get("codeNode");
      if (codeNode != null) {
        code = codeNode.asText();
        log.error("code: " + code);
      }
      JsonNode messageNode = error.get("message");
      if (messageNode != null) {
        message = messageNode.asText();
        log.error("message: " + message);
      }
      throw new RuntimeException("Error executing Cypher query:" + errorMessage + ":" + code + ":" + message);
    }
    return true;
  }

  protected long count(JsonNode jsonNode) {
    JsonNode countNode = jsonNode.at("/results/0/data/0/row/0");
    if (countNode != null && !countNode.isMissingNode()) {
      return countNode.asLong();
    } else {
      return -1;
    }
  }

  protected FolderServerArc buildArc(JsonNode a) {
    FolderServerArc arc = null;
    if (a != null && !a.isMissingNode()) {
      arc = new FolderServerArc(a.at("/0").textValue(), RelationLabel.forValue(a.at("/1").textValue()), a.at("/2")
          .textValue());
    }
    return arc;
  }

}