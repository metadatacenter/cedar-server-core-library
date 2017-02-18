package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

public class MissingPermissionException extends CedarAccessException {

  public MissingPermissionException(String permissionName) {
    super("Missing permission: '" + permissionName + "'.",
        CedarErrorKey.PERMISSION_MISSING,
        CedarSuggestedAction.REQUEST_ROLE);
    errorPack.parameter("permissionName", permissionName);
  }
}
