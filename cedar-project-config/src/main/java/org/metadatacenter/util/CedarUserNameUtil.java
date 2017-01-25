package org.metadatacenter.util;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.security.model.user.CedarUserRepresentation;

public class CedarUserNameUtil {

  private CedarUserNameUtil() {

  }

  public static String getDisplayName(CedarConfig cedarConfig, CedarUserRepresentation ur) {
    if (ur != null) {
      String screenName = cedarConfig.getBlueprintUserProfile().getScreenNameTemplate();
      if (screenName != null) {
        screenName = screenName.replace("{firstName}", ur.getFirstName() == null ? "" : ur.getFirstName());
        screenName = screenName.replace("{lastName}", ur.getLastName() == null ? "" : ur.getLastName());
      }
      return screenName;
    } else {
      return null;
    }
  }

  public static String getHomeFolderDescription(CedarConfig cedarConfig, CedarUserRepresentation ur) {
    String homeDescription = cedarConfig.getBlueprintUserProfile().getHomeFolderDescriptionTemplate();
    homeDescription = homeDescription.replace("{firstName}", ur.getFirstName());
    homeDescription = homeDescription.replace("{lastName}", ur.getLastName());
    return homeDescription;
  }
}
