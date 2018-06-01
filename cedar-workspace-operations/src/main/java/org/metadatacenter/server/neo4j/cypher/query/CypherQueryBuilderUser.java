package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public class CypherQueryBuilderUser extends AbstractCypherQueryBuilder {

  public static String createUser() {
    return "" +
        " CREATE (user:<COMPOSEDLABEL.USER> {" +
        buildCreateAssignment(NodeProperty.ID) + "," +
        buildCreateAssignment(NodeProperty.NAME) + "," +
        buildCreateAssignment(NodeProperty.FIRST_NAME) + "," +
        buildCreateAssignment(NodeProperty.LAST_NAME) + "," +
        buildCreateAssignment(NodeProperty.EMAIL) + "," +
        buildCreateAssignment(NodeProperty.CREATED_ON) + "," +
        buildCreateAssignment(NodeProperty.CREATED_ON_TS) + "," +
        buildCreateAssignment(NodeProperty.LAST_UPDATED_ON) + "," +
        buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS) + "," +
        buildCreateAssignment(NodeProperty.NODE_TYPE) +
        " })" +
        " RETURN user";
  }

  public static String findUsers() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " RETURN user" +
        " ORDER BY LOWER(user.<PROP.NAME>)";
  }

  public static String getUserById() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{id}})" +
        " RETURN user";
  }

  public static String addUserToGroup() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MERGE (user)-[:<REL.MEMBEROF>]->(group)" +
        " RETURN user";
  }


}
