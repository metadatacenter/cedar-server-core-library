package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.id.CedarCategoryId;
import org.metadatacenter.id.CedarUserId;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderCategoryPermission;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderPermission;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.security.model.permission.category.CategoryPermission;

import java.util.List;

public class Neo4JProxyCategoryPermission extends AbstractNeo4JProxy {

  Neo4JProxyCategoryPermission(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  void addCategoryPermissionToUser(CedarCategoryId categoryId, String userId, CategoryPermission permission) {
    FolderServerUser user = proxies.user().findUserById(userId);
    if (user != null) {
      FolderServerCategory category = proxies.category().getCategoryById(categoryId);
      if (category != null) {
        addCategoryPermission(category, user, permission);
      }
    }
  }

  void removeCategoryPermissionFromUser(CedarCategoryId categoryId, String userId,
                                        CategoryPermission permission) {
    FolderServerUser user = proxies.user().findUserById(userId);
    if (user != null) {
      FolderServerCategory category = proxies.category().getCategoryById(categoryId);
      if (category != null) {
        removeCategoryPermission(category, user, permission);
      }
    }
  }

  void addCategoryPermissionToGroup(CedarCategoryId categoryId, String groupId, CategoryPermission permission) {
    FolderServerGroup group = proxies.group().findGroupById(groupId);
    if (group != null) {
      FolderServerCategory category = proxies.category().getCategoryById(categoryId);
      if (category != null) {
        addCategoryPermission(category, group, permission);
      }
    }
  }

  void removeCategoryPermissionFromGroup(CedarCategoryId categoryId, String groupId,
                                         CategoryPermission permission) {
    FolderServerGroup group = proxies.group().findGroupById(groupId);
    if (group != null) {
      FolderServerCategory category = proxies.category().getCategoryById(categoryId);
      if (category != null) {
        removeCategoryPermission(category, group, permission);
      }
    }
  }

  private boolean addCategoryPermission(FolderServerCategory category, FolderServerUser user,
                                        CategoryPermission permission) {
    String cypher = CypherQueryBuilderCategoryPermission.addPermissionToCategoryForUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndCategoryId(user.getIdObject(),
        category.getIdObject());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "adding permission");
  }

  private boolean addCategoryPermission(FolderServerCategory category, FolderServerGroup group,
                                        CategoryPermission permission) {
    String cypher = CypherQueryBuilderCategoryPermission.addPermissionToCategoryForGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchGroupIdAndCategoryId(group.getIdObject(),
        category.getIdObject());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "adding permission");
  }

  private boolean removeCategoryPermission(FolderServerCategory category, FolderServerUser user,
                                           CategoryPermission permission) {
    String cypher = CypherQueryBuilderCategoryPermission.removePermissionForCategoryFromUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndCategoryId(user.getIdObject(),
        category.getIdObject());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing permission");
  }

  private boolean removeCategoryPermission(FolderServerCategory category, FolderServerGroup group,
                                           CategoryPermission permission) {
    String cypher = CypherQueryBuilderCategoryPermission.removePermissionForCategoryFromGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchGroupIdAndCategoryId(group.getIdObject(),
        category.getIdObject());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing permission");
  }

  public boolean userHasWriteAccessToCategory(CedarUserId userId, CedarCategoryId categoryId) {
    String cypher = CypherQueryBuilderPermission.userCanWriteCategory();
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndCategoryId(userId, categoryId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    FolderServerUser cedarFSUser = executeReadGetOne(q, FolderServerUser.class);
    return cedarFSUser != null;
  }

  public boolean userHasAttachAccessToCategory(CedarUserId userId, CedarCategoryId categoryId) {
    String cypher = CypherQueryBuilderPermission.userCanAttachCategory();
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndCategoryId(userId, categoryId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    FolderServerUser cedarFSUser = executeReadGetOne(q, FolderServerUser.class);
    return cedarFSUser != null;
  }

  public List<FolderServerUser> getUsersWithDirectPermissionOnCategory(CedarCategoryId categoryId, CategoryPermission permission) {
    RelationLabel relationLabel = null;
    switch (permission) {
      case ATTACH:
        relationLabel = RelationLabel.CANATTACHCATEGORY;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITECATEGORY;
        break;
    }
    String cypher = CypherQueryBuilderPermission.getUsersWithDirectPermissionOnCategory(relationLabel);
    CypherParameters params = CypherParamBuilderNode.matchId(categoryId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerUser.class);
  }

  public List<FolderServerGroup> getGroupsWithDirectPermissionOnCategory(CedarCategoryId categoryId, CategoryPermission permission) {
    RelationLabel relationLabel = null;
    switch (permission) {
      case ATTACH:
        relationLabel = RelationLabel.CANATTACHCATEGORY;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilderPermission.getGroupsWithDirectPermissionOnCategory(relationLabel);
    CypherParameters params = CypherParamBuilderNode.matchId(categoryId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerGroup.class);
  }

}
