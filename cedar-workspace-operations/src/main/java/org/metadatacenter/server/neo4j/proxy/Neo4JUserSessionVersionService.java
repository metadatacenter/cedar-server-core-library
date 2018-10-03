package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.VersionServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;
import org.metadatacenter.server.security.model.user.CedarUser;

public class Neo4JUserSessionVersionService extends AbstractNeo4JUserSession implements VersionServiceSession {

  public Neo4JUserSessionVersionService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    super(cedarConfig, proxies, cu);
  }

  public static VersionServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser) {
    return new Neo4JUserSessionVersionService(cedarConfig, proxies, cedarUser);
  }

  @Override
  public boolean userCanPerformVersioning(ResourceWithCurrentUserPermissions resource) {
    return userIsOwnerOfNode(resource.getId()) && resource.getType().isVersioned();
  }

  @Override
  public boolean resourceCanBePublished(ResourceWithCurrentUserPermissions resource) {
    if (resource.getPublicationStatus() == BiboStatus.DRAFT) {
      FolderServerResource nextVersion = proxies.version().resourceWithPreviousVersion(resource.getId());
      return nextVersion == null;
    }
    return false;
  }

  @Override
  public boolean resourceCanBeDrafted(ResourceWithCurrentUserPermissions resource) {
    if (resource.getPublicationStatus() == BiboStatus.PUBLISHED) {
      FolderServerResource nextVersion = proxies.version().resourceWithPreviousVersion(resource.getId());
      return nextVersion == null;
    }
    return false;
  }

}
