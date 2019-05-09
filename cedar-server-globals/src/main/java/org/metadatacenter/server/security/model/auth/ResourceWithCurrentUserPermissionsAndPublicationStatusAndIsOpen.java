package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.NodeWithIsOpen;
import org.metadatacenter.server.security.model.NodeWithPublicationStatus;

public interface ResourceWithCurrentUserPermissionsAndPublicationStatusAndIsOpen
    extends ResourceWithCurrentUserPermissions, NodeWithPublicationStatus, NodeWithIsOpen {
}
