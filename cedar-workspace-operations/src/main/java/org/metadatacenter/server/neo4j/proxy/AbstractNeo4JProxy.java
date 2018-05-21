package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.neo4j.driver.v1.types.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  protected long executeReadGetCount(CypherQuery q) {
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
      if (record != null) {
        Value value = record.get(0);
        if (value.type().equals(session.typeSystem().INTEGER())) {
          return value.asLong();
        }
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }

    return -1;
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
      if (record != null) {
        Node n = record.get(0).asNode();
        if (n != null) {
          JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
          return buildClass(node, type);
        }
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
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

      if (records != null) {
        for (Record r : records) {
          if (r.size() == 1) {
            Value value = r.get(0);
            if (value.type().equals(session.typeSystem().NODE())) {
              Node n = value.asNode();
              if (n != null) {
                JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
                T folderServerNode = buildClass(node, type);
                folderServerNodeList.add(folderServerNode);
              }
            } else if (value.type().equals(session.typeSystem().PATH())) {
              Path segments = value.asPath();
              for (Node n : segments.nodes()) {
                JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
                T folderServerNode = buildClass(node, type);
                folderServerNodeList.add(folderServerNode);
              }
            }
          } else {
            for (Value value : r.values()) {
              if (value.type().equals(session.typeSystem().NODE())) {
                Node n = value.asNode();
                if (n != null) {
                  JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
                  T folderServerNode = buildClass(node, type);
                  folderServerNodeList.add(folderServerNode);
                }
              }
            }
          }
        }
        return folderServerNodeList;
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }

    return folderServerNodeList;
  }

  protected List<FolderServerArc> executeReadGetArcList(CypherQuery q) {
    List<FolderServerArc> folderServerArcList = new ArrayList<>();
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

      if (records != null) {
        for (Record r : records) {
          Map<String, Object> recordMap = r.asMap();
          if (recordMap != null) {
            JsonNode node = JsonMapper.MAPPER.valueToTree(recordMap);
            FolderServerArc rel = buildArc(node);
            folderServerArcList.add(rel);
          }
        }
        return folderServerArcList;
      }
    } catch (ClientException ex) {
      log.error("Error executing Cypher query:", ex);
      throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
    }

    return folderServerArcList;
  }


  private <T extends CedarNode> T buildClass(JsonNode node, Class<T> type) {
    if (type == FolderServerUser.class) {
      return (T) buildUser(node);
    } else if (type == FolderServerGroup.class) {
      return (T) buildGroup(node);
    } else if (type == FolderServerFolder.class) {
      return (T) buildFolder(node);
    } else if (type == FolderServerResource.class) {
      return (T) buildResource(node);
    } else if (type == FolderServerNode.class) {
      return (T) buildNode(node);
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

  protected FolderServerArc buildArc(JsonNode a) {
    FolderServerArc arc = null;
    if (a != null && !a.isMissingNode()) {
      arc = new FolderServerArc(a.at("/sid").textValue(), RelationLabel.forValue(a.at("/type").textValue()), a.at("/tid")
          .textValue());
    }
    return arc;
  }

}