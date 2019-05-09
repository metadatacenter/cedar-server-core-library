package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderVersion;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

public class Neo4JProxyVersion extends AbstractNeo4JProxy {

  Neo4JProxyVersion(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  public FileSystemResource resourceWithPreviousVersion(String resourceURL) {
    String cypher = CypherQueryBuilderVersion.getResourceWithPreviousVersion();
    CypherParameters params = CypherParamBuilderNode.getNodeById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FileSystemResource.class);
  }

}
