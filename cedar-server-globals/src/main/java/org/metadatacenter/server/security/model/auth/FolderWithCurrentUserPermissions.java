package org.metadatacenter.server.security.model.auth;

public interface FolderWithCurrentUserPermissions extends NodeWithCurrentUserPermissions {

  boolean isRoot();

  boolean isSystem();

  boolean isUserHome();
}
