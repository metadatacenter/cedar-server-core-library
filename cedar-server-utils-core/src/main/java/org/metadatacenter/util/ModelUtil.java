package org.metadatacenter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelUtil {

  private static final String specialFieldPattern = "(^@)|(^_)|(^schema:)|(^pav:)|(^oslc:)";

  private ModelUtil() {}

  public static boolean isSpecialField(String fieldName) {
    // Create a Pattern object
    Pattern r = Pattern.compile(specialFieldPattern);
    // Now create matcher object.
    Matcher m = r.matcher(fieldName);
    if (m.find()) {
      return true;
    } else {
      return false;
    }
  }

}



