package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarErrorKey {

  NONE(null),

  TEMPLATE_ELEMENT_NOT_CREATED("templateElementNotCreated"),
  TEMPLATE_ELEMENT_NOT_FOUND("templateElementNotFound"),
  TEMPLATE_ELEMENT_NOT_DELETED("templateElementNotDeleted"),
  TEMPLATE_ELEMENT_NOT_UPDATED("templateElementNotUpdated"),
  TEMPLATE_ELEMENTS_NOT_LISTED("templateElementsNotListed"),

  TEMPLATE_FIELD_NOT_CREATED("templateFieldNotCreated"),
  TEMPLATE_FIELD_NOT_FOUND("templateFieldNotFound"),
  TEMPLATE_FIELD_NOT_DELETED("templateFieldNotDeleted"),
  TEMPLATE_FIELD_NOT_UPDATED("templateFieldNotUpdated"),
  TEMPLATE_FIELDS_NOT_LISTED("templateFieldsNotListed"),

  TEMPLATE_NOT_CREATED("templateNotCreated"),
  TEMPLATE_NOT_FOUND("templateNotFound"),
  TEMPLATE_NOT_DELETED("templateNotDeleted"),
  TEMPLATE_NOT_UPDATED("templateNotUpdated"),
  TEMPLATES_NOT_LISTED("templatesNotListed"),

  TEMPLATE_INSTANCE_NOT_CREATED("templateInstanceNotCreated"),
  TEMPLATE_INSTANCE_NOT_FOUND("templateInstanceNotFound"),
  TEMPLATE_INSTANCE_NOT_DELETED("templateInstanceNotDeleted"),
  TEMPLATE_INSTANCE_NOT_UPDATED("templateInstanceNotUpdated"),
  TEMPLATE_INSTANCES_NOT_LISTED("templateInstancesNotListed"),

  NO_READ_ACCESS_TO_FOLDER("noReadAccessToFolder"),
  NO_WRITE_ACCESS_TO_FOLDER("noWriteAccessToFolder"),

  NO_READ_ACCESS_TO_TEMPLATE("noReadAccessToTemplate"),
  NO_WRITE_ACCESS_TO_TEMPLATE("noWriteAccessToTemplate"),

  NO_READ_ACCESS_TO_TEMPLATE_ELEMENT("noReadAccessToTemplateElement"),
  NO_WRITE_ACCESS_TO_TEMPLATE_ELEMENT("noWriteAccessToTemplateElement"),

  NO_READ_ACCESS_TO_TEMPLATE_FIELD("noReadAccessToTemplateField"),
  NO_WRITE_ACCESS_TO_TEMPLATE_FIELD("noWriteAccessToTemplateField"),

  NO_READ_ACCESS_TO_TEMPLATE_INSTANCE("noReadAccessToTemplateInstance"),
  NO_WRITE_ACCESS_TO_TEMPLATE_INSTANCE("noWriteAccessToTemplateInstance"),

  NO_READ_ACCESS_TO_RESOURCE("noReadAccessToResource"),
  NO_WRITE_ACCESS_TO_RESOURCE("noWriteAccessToResource"),

  FOLDER_NOT_FOUND("folderNotFound"),

  RESOURCE_NOT_FOUND("resourceNotFound"),

  UNKNOWN_NODE_TYPE("unknownNodeType"),

  MISSING_PARAMETER("missingParameter"),

  INVALID_INPUT("invalidInput"),

  SOURCE_FOLDER_NOT_FOUND("sourceFolderNotFound"),
  SOURCE_RESOURCE_NOT_FOUND("sourceResourceNotFound"),
  TARGET_FOLDER_NOT_FOUND("targetFolderNotFound"),

  NODE_NOT_MOVED("nodeNotMoved"),
  GROUP_NOT_FOUND("groupNotFound"),
  USER_NOT_FOUND("userNotFound"),

  UNIQUE_CONSTRAINT_COLLISION("uniqueConstraintCollision"),
  INVALID_DATA("invalidData"),
  NOT_AUTHORIZED("notAuthorized"),

  PERMISSION_MISSING("permissionMissing"),
  TOKEN_INVALID("tokenInvalid"),
  USER_INFO_LOAD_BY_TOKEN_FAILED("userInfoLoadByTokenFailed"),
  USER_INFO_LOAD_BY_API_KEY_FAILED("userInfoLoadByApiKeyFailed"),
  CEDAR_USER_NOT_FOUND("cedarUserNotFound"),
  AUTHORIZATION_TYPE_NOT_FOUND("authorizationTypeNotFound"),
  AUTHORIZATION_NOT_FOUND("authorizationNotFound"),
  API_KEY_NOT_FOUND("apiKeyNotFound"),
  TOKEN_MISSING("tokenMissing"),
  TOKEN_EXPIRED("tokenExpired"),
  PARENT_FOLDER_NOT_SPECIFIED("parentFolderNotSpecified"),
  PARENT_FOLDER_SPECIFIED_TWICE("parentFolderSpecifiedTwice"),
  PATH_NOT_NORMALIZED("pathNotNormalized"),
  PARENT_FOLDER_NOT_FOUND("parentFolderNotFound"),
  INVALID_FOLDER_NAME("invalidFolderName"),
  NODE_ALREADY_PRESENT("nodeAlreadyPresent"),
  FOLDER_NOT_CREATED("folderNotCreated"),
  MISSING_NAME_AND_DESCRIPTION("missingNameAndDescription"),
  FOLDER_NOT_DELETED("folderNotDeleted"),
  RESOURCE_NOT_DELETED("resourceNotDeleted"),
  RESOURCE_NOT_CREATED("resourceNotCreated"),
  INVALID_NODE_TYPE("invalidNodeType"),
  READ_OTHER_PROFILE_FORBIDDEN("readOtherProfileForbidden"),
  UPDATE_OTHER_PROFILE_FORBIDDEN("updateOtherProfileForbidden"),
  FOLDER_CAN_NOT_BE_DELETED("folderCanNotBeDeleted");

  private final String value;

  CedarErrorKey(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
