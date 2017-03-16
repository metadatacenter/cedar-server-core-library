package org.metadatacenter.util;

import java.text.Normalizer;

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



