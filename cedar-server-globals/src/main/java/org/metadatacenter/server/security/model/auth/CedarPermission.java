package org.metadatacenter.server.security.model.auth;


import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.security.model.CedarObjectConstants;

public enum CedarPermission {

  TEMPLATE_CREATE(CedarNodeType.Types.TEMPLATE, CedarObjectConstants.ACCESS_CREATE),
  TEMPLATE_READ(CedarNodeType.Types.TEMPLATE, CedarObjectConstants.ACCESS_READ),
  TEMPLATE_UPDATE(CedarNodeType.Types.TEMPLATE, CedarObjectConstants.ACCESS_UPDATE),
  TEMPLATE_DELETE(CedarNodeType.Types.TEMPLATE, CedarObjectConstants.ACCESS_DELETE),

  TEMPLATE_ELEMENT_CREATE(CedarNodeType.Types.ELEMENT, CedarObjectConstants.ACCESS_CREATE),
  TEMPLATE_ELEMENT_READ(CedarNodeType.Types.ELEMENT, CedarObjectConstants.ACCESS_READ),
  TEMPLATE_ELEMENT_UPDATE(CedarNodeType.Types.ELEMENT, CedarObjectConstants.ACCESS_UPDATE),
  TEMPLATE_ELEMENT_DELETE(CedarNodeType.Types.ELEMENT, CedarObjectConstants.ACCESS_DELETE),

  TEMPLATE_FIELD_CREATE(CedarNodeType.Types.FIELD, CedarObjectConstants.ACCESS_CREATE),
  TEMPLATE_FIELD_READ(CedarNodeType.Types.FIELD, CedarObjectConstants.ACCESS_READ),
  TEMPLATE_FIELD_UPDATE(CedarNodeType.Types.FIELD, CedarObjectConstants.ACCESS_UPDATE),
  TEMPLATE_FIELD_DELETE(CedarNodeType.Types.FIELD, CedarObjectConstants.ACCESS_DELETE),

  TEMPLATE_INSTANCE_CREATE(CedarNodeType.Types.INSTANCE, CedarObjectConstants.ACCESS_CREATE),
  TEMPLATE_INSTANCE_READ(CedarNodeType.Types.INSTANCE, CedarObjectConstants.ACCESS_READ),
  TEMPLATE_INSTANCE_UPDATE(CedarNodeType.Types.INSTANCE, CedarObjectConstants.ACCESS_UPDATE),
  TEMPLATE_INSTANCE_DELETE(CedarNodeType.Types.INSTANCE, CedarObjectConstants.ACCESS_DELETE),

  FOLDER_CREATE(CedarNodeType.Types.FOLDER, CedarObjectConstants.ACCESS_CREATE),
  FOLDER_READ(CedarNodeType.Types.FOLDER, CedarObjectConstants.ACCESS_READ),
  FOLDER_UPDATE(CedarNodeType.Types.FOLDER, CedarObjectConstants.ACCESS_UPDATE),
  FOLDER_DELETE(CedarNodeType.Types.FOLDER, CedarObjectConstants.ACCESS_DELETE),

  LOGGED_IN(CedarObjectConstants.LOGGED_IN, CedarObjectConstants.ACCESS_READ),
  SEARCH_INDEX_REINDEX(CedarObjectConstants.SEARCH_INDEX, CedarObjectConstants.ACCESS_CREATE),
  SYSTEM_FOLDER_CREATE(CedarObjectConstants.SYSTEM_FOLDER, CedarObjectConstants.ACCESS_CREATE),
  USER_PROFILE_OWN_READ(CedarObjectConstants.USER_PROFILE_OWN, CedarObjectConstants.ACCESS_READ);

  private final String resourceType;
  private final String accessType;
  private final String permissionName;

  CedarPermission(String resourceType, String accessType) {
    this.resourceType = resourceType;
    this.accessType = accessType;
    StringBuilder sb = new StringBuilder();
    sb.append("permission_");
    sb.append(resourceType);
    sb.append("_");
    sb.append(accessType);
    permissionName = sb.toString();
  }

  public String getAccessType() {
    return accessType;
  }

  public String getResourceType() {
    return resourceType;
  }

  public String getPermissionName() {
    return permissionName;
  }
}
