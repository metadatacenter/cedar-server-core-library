package org.metadatacenter.util;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.security.model.user.ICedarUserRepresentation;

public class CedarUserNameUtil {

  private CedarUserNameUtil() {

  }

  public static String getDisplayName(ICedarUserRepresentation ur) {
    if (ur != null) {
      CedarConfig cedarConfig = CedarConfig.getInstance();
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

  public static String getHomeFolderDescription(ICedarUserRepresentation ur) {
    CedarConfig cedarConfig = CedarConfig.getInstance();
    String homeDescription = cedarConfig.getBlueprintUserProfile().getHomeFolderDescriptionTemplate();
    homeDescription = homeDescription.replace("{firstName}", ur.getFirstName());
    homeDescription = homeDescription.replace("{lastName}", ur.getLastName());
    return homeDescription;
  }
}
