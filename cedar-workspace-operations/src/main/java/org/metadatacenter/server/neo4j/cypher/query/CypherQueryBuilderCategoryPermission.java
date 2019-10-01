package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.server.security.model.permission.category.CategoryPermission;

public class CypherQueryBuilderCategoryPermission extends AbstractCypherQueryBuilder {

  public static String addPermissionToCategoryForUser(CategoryPermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (category:<LABEL.CATEGORY> {<PROP.ID>:{categoryId}})" +
        " CREATE (user)-[:" + RelationLabel.forCategoryPermission(permission) + "]->(category)" +
        " RETURN user";
  }

  public static String addPermissionToCategoryForGroup(CategoryPermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (category:<LABEL.CATEGORY> {<PROP.ID>:{categoryId}})" +
        " CREATE (group)-[:" + RelationLabel.forCategoryPermission(permission) + "]->(category)" +
        " RETURN group";
  }

  public static String removePermissionForCategoryFromUser(CategoryPermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (category:<LABEL.CATEGORY> {<PROP.ID>:{categoryId}})" +
        " MATCH (user)-[relation:" + RelationLabel.forCategoryPermission(permission) + "]->(category)" +
        " DELETE (relation)" +
        " RETURN category";
  }

  public static String removePermissionForCategoryFromGroup(CategoryPermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (category:<LABEL.CATEGORY> {<PROP.ID>:{categoryId} })" +
        " MATCH (group)-[relation:" + RelationLabel.forCategoryPermission(permission) + "]->(category)" +
        " DELETE (relation)" +
        " RETURN category";
  }

}
