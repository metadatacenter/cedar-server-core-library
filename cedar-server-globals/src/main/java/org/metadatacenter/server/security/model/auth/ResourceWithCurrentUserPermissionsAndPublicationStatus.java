package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.NodeWithPublicationStatus;

public interface ResourceWithCurrentUserPermissionsAndPublicationStatus
    extends ResourceWithCurrentUserPermissions, NodeWithPublicationStatus {
}
