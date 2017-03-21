package org.metadatacenter.model.request;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.exception.CedarException;

import java.util.Optional;

public class OutputFormatTypeDetector {
  public static OutputFormatType detectFormat(Optional<String> format) throws CedarException {
    OutputFormatType formatType = OutputFormatType.JSONLD;
    if (format != null && format.isPresent() && !format.get().isEmpty()) {
      formatType = OutputFormatType.forValue(format.get());
    }
    return checkNotUnknownFormat(formatType);
  }

  private static OutputFormatType checkNotUnknownFormat(OutputFormatType formatType) throws CedarException {
    if (formatType == null) {
      return unknownOutputFormatException();
    }
    return formatType;
  }

  private static OutputFormatType unknownOutputFormatException() throws CedarException {
    final CedarErrorPack errorPack = new CedarErrorPack();
    errorPack.errorKey(CedarErrorKey.UNKNOWN_INSTANCE_OUTPUT_FORMAT)
        .errorType(CedarErrorType.INVALID_ARGUMENT)
        .message("Unknown requested output format")
        .parameter("expectedFormats", OutputFormatType.values());
    throw new CedarException(errorPack){};
  }
}
