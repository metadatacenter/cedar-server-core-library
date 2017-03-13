package org.metadatacenter.model.request;

import java.util.Optional;

public class OutputFormatTypeDetector {
  public static OutputFormatType detectFormat(Optional<String> format) {
    if (format != null && format.isPresent() && !format.get().isEmpty()) {
      return OutputFormatType.forValue(format.get());
    }
    return OutputFormatType.JSONLD;
  }
}
