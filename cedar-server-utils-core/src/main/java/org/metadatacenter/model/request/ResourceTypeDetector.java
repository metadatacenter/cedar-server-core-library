package org.metadatacenter.model.request;

import com.google.common.base.Strings;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.exception.CedarException;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResourceTypeDetector {
  public static ResourceType detectType(String resourceType) throws CedarException {
    checkNotNull(resourceType);
    ResourceType nodeType = null;
    if (!Strings.isNullOrEmpty(resourceType)) {
      nodeType = ResourceType.forValue(resourceType);
    }
    return checkNotUnknownType(nodeType);
  }

  private static ResourceType checkNotUnknownType(ResourceType nodeType) throws CedarException {
    if (nodeType == null) {
      throw unknownNodeTypeException();
    }
    return nodeType;
  }

  private static CedarException unknownNodeTypeException() {
    final CedarErrorPack errorPack = new CedarErrorPack();
    errorPack.errorKey(CedarErrorKey.UNKNOWN_NODE_TYPE)
        .errorType(CedarErrorType.INVALID_ARGUMENT)
        .message("Unknown requested resource type")
        .parameter("expectedTypes", ResourceType.values());
    return new CedarException(errorPack) {
    };
  }
}
