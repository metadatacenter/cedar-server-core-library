package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.metadatacenter.model.CedarNode;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.FolderServerArc;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.server.logging.AppLogger;
import org.metadatacenter.server.logging.model.AppLogMessage;
import org.metadatacenter.server.logging.model.AppLogParam;
import org.metadatacenter.server.logging.model.AppLogSubType;
import org.metadatacenter.server.logging.model.AppLogType;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.log.CypherQueryLog;
import org.metadatacenter.server.neo4j.util.Neo4JUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
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

  private void reportQueryError(ClientException ex, CypherQuery q) {
    log.error("Error executing Cypher query:", ex);
    log.error(q.getOriginalQuery());
    if (q instanceof CypherQueryWithParameters) {
      log.error(((CypherQueryWithParameters) q).getParameterMap().toString());
    }
    log.error(q.getRunnableQuery());
    throw new RuntimeException("Error executing Cypher query:" + ex.getMessage());
  }

  protected boolean executeWrite(CypherQuery q, String eventDescription) {
    boolean result = false;
    try (Session session = driver.session()) {
      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        final String runnableQuery = qp.getRunnableQuery();
        final Map<String, Object> parameterMap = qp.getParameterMap();
        CypherQueryLog queryLog = prepareQueryLog("write", qp);
        result = session.writeTransaction(tx -> {
          tx.run(runnableQuery, parameterMap);
          return true;
        });
        commitQueryLog(queryLog);
      } else if (q instanceof CypherQueryLiteral) {
        final String runnableQuery = q.getRunnableQuery();
        CypherQueryLog queryLog = prepareQueryLog("write", q);
        result = session.writeTransaction(tx -> {
          tx.run(runnableQuery);
          return true;
        });
        commitQueryLog(queryLog);
      }
    } catch (ClientException ex) {
      log.error("Error while " + eventDescription, ex);
      reportQueryError(ex, q);
    }
    return result;
  }

  private CypherQueryLog prepareQueryLog(String operation, CypherQueryWithParameters qp) {
    CypherQueryLog log = new CypherQueryLog(operation,
        qp.getOriginalQuery(),
        qp.getRunnableQuery(),
        qp.getParameterMap(),
        qp.getInterpolatedParamsQuery());
    log.setStart(Instant.now());
    return log;
  }

  private CypherQueryLog prepareQueryLog(String operation, CypherQuery q) {
    CypherQueryLog log = new CypherQueryLog(operation,
        q.getOriginalQuery(),
        q.getRunnableQuery(),
        null,
        q.getRunnableQuery());
    log.setStart(Instant.now());
    return log;
  }

  private void commitQueryLog(CypherQueryLog log) {
    log.setEnd(Instant.now());

    String paramMapString = null;
    try {
      paramMapString = JsonMapper.PRETTY_MAPPER.writeValueAsString(log.getParameterMap());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    AppLogMessage appLog = AppLogger.message(AppLogType.CYPHER_QUERY, AppLogSubType.FULL)
        .param(AppLogParam.ORIGINAL_QUERY, log.getOriginalQuery())
        .param(AppLogParam.RUNNABLE_QUERY, log.getRunnableQuery())
        .param(AppLogParam.INTERPOLATED_QUERY, log.getInterpolatedParamsQuery())
        .param(AppLogParam.QUERY_PARAMETERS, log.getParameterMap())
        .param(AppLogParam.RUNNABLE_QUERY_HASH, DigestUtils.md5Hex(log.getRunnableQuery()))
        .param(AppLogParam.QUERY_PARAMETERS_HASH, DigestUtils.md5Hex(paramMapString))
        .param(AppLogParam.START_TIME, log.getStart())
        .param(AppLogParam.OPERATION, log.getOperation());
    appLog.setDuration(Duration.between(log.getStart(), log.getEnd()));

    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    int count = 0;
    for (int i = stackTrace.length - 1; i >= 0; i--) {
      StackTraceElement ste = stackTrace[i];
      if (count == 0 && (ste.getClassName().contains("Neo4JUser") || ste.getClassName().contains("Neo4JProxy"))) {
        appLog.param(AppLogParam.CLASS_NAME, ste.getClassName());
        appLog.param(AppLogParam.METHOD_NAME, ste.getMethodName());
        count++;
      }
    }
    appLog.enqueue();
  }

  protected <T extends CedarNode> T executeWriteGetOne(CypherQuery q, Class<T> type) {
    Record record = null;
    try (Session session = driver.session()) {
      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        final String runnableQuery = qp.getRunnableQuery();
        final Map<String, Object> parameterMap = qp.getParameterMap();
        CypherQueryLog queryLog = prepareQueryLog("writeGetOne", qp);
        record = session.writeTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery, parameterMap);
          return result.hasNext() ? result.next() : null;
        });
        commitQueryLog(queryLog);
      } else if (q instanceof CypherQueryLiteral) {
        final String runnableQuery = q.getRunnableQuery();
        CypherQueryLog queryLog = prepareQueryLog("writeGetOne", q);
        record = session.writeTransaction(tx -> {
          StatementResult result = tx.run(runnableQuery);
          return result.hasNext() ? result.next() : null;
        });
        commitQueryLog(queryLog);
      }
    } catch (ClientException ex) {
      reportQueryError(ex, q);
    }

    return extractClassFromRecord(record, type);
  }

  private <T extends CedarNode> T extractClassFromRecord(Record record, Class<T> type) {
    if (record != null) {
      Node n = record.get(0).asNode();
      if (n != null) {
        JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
        return buildClass(node, type);
      }
    }
    return null;
  }

  private Record executeQueryGetRecord(Session session, CypherQuery q) {
    Record record = null;
    if (q instanceof CypherQueryWithParameters) {
      CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
      final String runnableQuery = qp.getRunnableQuery();
      final Map<String, Object> parameterMap = qp.getParameterMap();
      CypherQueryLog queryLog = prepareQueryLog("getRecord", qp);
      record = session.readTransaction(tx -> {
        StatementResult result = tx.run(runnableQuery, parameterMap);
        return result.hasNext() ? result.next() : null;
      });
      commitQueryLog(queryLog);
    } else if (q instanceof CypherQueryLiteral) {
      final String runnableQuery = q.getRunnableQuery();
      CypherQueryLog queryLog = prepareQueryLog("getRecord", q);
      record = session.readTransaction(tx -> {
        StatementResult result = tx.run(runnableQuery);
        return result.hasNext() ? result.next() : null;
      });
      commitQueryLog(queryLog);
    }
    return record;
  }

  protected long executeReadGetCount(CypherQuery q) {
    try (Session session = driver.session()) {
      Record record = executeQueryGetRecord(session, q);
      if (record != null) {
        Value value = record.get(0);
        if (value.type().equals(session.typeSystem().INTEGER())) {
          return value.asLong();
        }
      }
    } catch (ClientException ex) {
      reportQueryError(ex, q);
    }

    return -1;
  }

  protected <T extends CedarNode> T executeReadGetOne(CypherQuery q, Class<T> type) {
    try (Session session = driver.session()) {
      Record record = executeQueryGetRecord(session, q);
      return extractClassFromRecord(record, type);
    } catch (ClientException ex) {
      reportQueryError(ex, q);
    }
    return null;
  }

  private List<Record> executeQueryGetRecordList(Session session, CypherQuery q) {
    List<Record> records = null;
    if (q instanceof CypherQueryWithParameters) {
      CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
      final String runnableQuery = qp.getRunnableQuery();
      final Map<String, Object> parameterMap = qp.getParameterMap();
      CypherQueryLog queryLog = prepareQueryLog("getRecordList", qp);
      records = session.readTransaction(tx -> {
        StatementResult result = tx.run(runnableQuery, parameterMap);
        List<Record> nodes = new ArrayList<>();
        while (result.hasNext()) {
          nodes.add(result.next());
        }
        return nodes;
      });
      commitQueryLog(queryLog);
    } else if (q instanceof CypherQueryLiteral) {
      final String runnableQuery = q.getRunnableQuery();
      CypherQueryLog queryLog = prepareQueryLog("getRecordList", q);
      records = session.readTransaction(tx -> {
        StatementResult result = tx.run(runnableQuery);
        List<Record> nodes = new ArrayList<>();
        while (result.hasNext()) {
          nodes.add(result.next());
        }
        return nodes;
      });
      commitQueryLog(queryLog);
    }
    return records;
  }


  protected <T extends CedarNode> List<T> executeReadGetList(CypherQuery q, Class<T> type) {
    List<T> folderServerNodeList = new ArrayList<>();
    try (Session session = driver.session()) {
      List<Record> records = executeQueryGetRecordList(session, q);
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
            } else if (value.type().equals(session.typeSystem().LIST())) {
              List<Object> list = value.asList();
              for (Object o : list) {
                if (o instanceof Node) {
                  Node n = (Node) o;
                  JsonNode node = JsonMapper.MAPPER.valueToTree(n.asMap());
                  T folderServerNode = buildClass(node, type);
                  folderServerNodeList.add(folderServerNode);
                }
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
      reportQueryError(ex, q);
    }

    return folderServerNodeList;
  }

  protected List<FolderServerArc> executeReadGetArcList(CypherQuery q) {
    List<FolderServerArc> folderServerArcList = new ArrayList<>();
    try (Session session = driver.session()) {
      List<Record> records = executeQueryGetRecordList(session, q);
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
      reportQueryError(ex, q);
    }

    return folderServerArcList;
  }


  private <T extends CedarNode> T buildClass(JsonNode node, Class<T> type) {
    T cn = null;
    if (node != null && !node.isMissingNode()) {
      try {
        JsonNode unescaped = Neo4JUtil.unescapeTopLevelPropertyNames(node);
        cn = JsonMapper.MAPPER.treeToValue(unescaped, type);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing node into " + type.getSimpleName(), e);
      }
    }
    return cn;
  }

  protected FolderServerFolder buildFolder(JsonNode f) {
    return buildClass(f, FolderServerFolder.class);
  }

  public FolderServerResource buildResource(JsonNode r) {
    return buildClass(r, FolderServerResource.class);
  }

  protected FolderServerNode buildNode(JsonNode n) {
    return buildClass(n, FolderServerNode.class);
  }

  protected FolderServerArc buildArc(JsonNode a) {
    FolderServerArc arc = null;
    if (a != null && !a.isMissingNode()) {
      arc = new FolderServerArc(a.at("/sid").textValue(), RelationLabel.forValue(a.at("/type").textValue()), a.at
          ("/tid")
          .textValue());
    }
    return arc;
  }

}