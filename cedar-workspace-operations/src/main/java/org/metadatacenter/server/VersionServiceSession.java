package org.metadatacenter.server;

import org.metadatacenter.outcome.OutcomeWithReason;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;

public interface VersionServiceSession {

  OutcomeWithReason userCanPerformVersioning(ResourceWithCurrentUserPermissions resource);

  OutcomeWithReason resourceCanBePublished(ResourceWithCurrentUserPermissions resource);

  OutcomeWithReason resourceCanBeDrafted(ResourceWithCurrentUserPermissions resource);
}