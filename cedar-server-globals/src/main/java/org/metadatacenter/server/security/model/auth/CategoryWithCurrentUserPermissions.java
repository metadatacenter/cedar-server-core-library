package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.ResourceWithIdAndType;

public interface CategoryWithCurrentUserPermissions extends ResourceWithIdAndType {

  boolean isRoot();

  CurrentUserCategoryPermissions getCurrentUserPermissions();
}
