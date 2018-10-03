package org.metadatacenter.server;

import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;

public interface VersionServiceSession {

  boolean userCanPerformVersioning(ResourceWithCurrentUserPermissions resource);

  boolean resourceCanBePublished(ResourceWithCurrentUserPermissions resource);

  boolean resourceCanBeDrafted(ResourceWithCurrentUserPermissions resource);
}