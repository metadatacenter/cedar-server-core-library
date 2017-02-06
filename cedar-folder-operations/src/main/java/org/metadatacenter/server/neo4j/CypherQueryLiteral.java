package org.metadatacenter.server.neo4j;

public class CypherQueryLiteral implements CypherQuery {
  private final String query;

  public CypherQueryLiteral(String query) {
    this.query = query;
  }

  @Override
  public String getQuery() {
    return query;
  }

  @Override
  public String getFlatQuery() {
    if (query != null) {
      return query.replace("\n", " ").replace("\r", " ");
    } else {
      return null;
    }
  }

}
