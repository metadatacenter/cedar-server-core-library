package org.metadatacenter.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

  private StringUtil() {
  }

  public static String stripAccents(String v) {
    if (v != null) {
      String s = Normalizer.normalize(v, Normalizer.Form.NFD);
      return s.replaceAll("\\p{M}", "");
    } else {
      return null;
    }
  }

  public static String comparisonValue(String v) {
    if (v != null) {
      return stripAccents(v).toLowerCase();
    } else {
      return null;
    }
  }

}



