package org.metadatacenter.server.security.model.permission.category;

import org.metadatacenter.server.security.model.ResourceWithIdAndType;
import org.metadatacenter.server.security.model.auth.CurrentUserCategoryPermissions;

public interface CategoryWithCurrentUserPermissions extends ResourceWithIdAndType {

  boolean isRoot();

  CurrentUserCategoryPermissions getCurrentUserPermissions();
}
