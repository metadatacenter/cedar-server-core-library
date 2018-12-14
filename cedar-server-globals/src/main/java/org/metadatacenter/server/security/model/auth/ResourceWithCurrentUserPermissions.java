package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.NodeWithIsPublic;
import org.metadatacenter.server.security.model.NodeWithPublicationStatus;

public interface ResourceWithCurrentUserPermissions
    extends NodeWithCurrentUserPermissions, NodeWithPublicationStatus, NodeWithIsPublic {
}
