package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderResource;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderVersion;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

public class Neo4JProxyVersion extends AbstractNeo4JProxy {

  Neo4JProxyVersion(Neo4JProxies proxies) {
    super(proxies);
  }

  public FolderServerResource resourceWithPreviousVersion(String resourceURL) {
    String cypher = CypherQueryBuilderVersion.getResourceWithPreviousVersion();
    CypherParameters params = CypherParamBuilderResource.getResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerResource.class);
  }

}