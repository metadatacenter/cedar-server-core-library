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

  GROUP_CREATE(CedarNodeType.Types.GROUP, CedarObjectConstants.ACCESS_CREATE),
  GROUP_READ(CedarNodeType.Types.GROUP, CedarObjectConstants.ACCESS_READ),
  GROUP_UPDATE(CedarNodeType.Types.GROUP, CedarObjectConstants.ACCESS_UPDATE),
  GROUP_DELETE(CedarNodeType.Types.GROUP, CedarObjectConstants.ACCESS_DELETE),

  USER_READ(CedarNodeType.Types.USER, CedarObjectConstants.ACCESS_READ),
  USER_UPDATE(CedarNodeType.Types.USER, CedarObjectConstants.ACCESS_UPDATE),

  LOGGED_IN(CedarObjectConstants.LOGGED_IN, CedarObjectConstants.ACCESS_READ),
  SEARCH_INDEX_REINDEX(CedarObjectConstants.SEARCH_INDEX, CedarObjectConstants.ACCESS_CREATE),

  UPDATE_PERMISSION_NOT_WRITABLE_NODE(CedarObjectConstants.NOT_WRITABLE_NODE_PERMISSIONS,
      CedarObjectConstants.ACCESS_UPDATE),
  CREATE_IN_NOT_WRITABLE_FOLDER(CedarObjectConstants.NOT_WRITABLE_FOLDER, CedarObjectConstants.ACCESS_CREATE),
  READ_NOT_READABLE_NODE(CedarObjectConstants.NOT_READABLE_NODE, CedarObjectConstants.ACCESS_READ),
  UPDATE_NOT_WRITABLE_NODE(CedarObjectConstants.NOT_WRITABLE_NODE, CedarObjectConstants.ACCESS_UPDATE),
  DELETE_NOT_WRITABLE_NODE(CedarObjectConstants.NOT_WRITABLE_NODE, CedarObjectConstants.ACCESS_DELETE);

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
