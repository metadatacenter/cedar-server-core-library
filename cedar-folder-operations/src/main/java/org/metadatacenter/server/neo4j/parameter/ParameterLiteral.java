package org.metadatacenter.server.neo4j.parameter;

public class ParameterLiteral implements CypherQueryParameter {

  private final String value;

  public ParameterLiteral(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}