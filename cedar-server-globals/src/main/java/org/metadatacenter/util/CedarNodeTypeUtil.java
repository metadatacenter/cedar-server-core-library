package org.metadatacenter.util;

import org.metadatacenter.model.CedarNodeType;

import java.util.*;

public class CedarNodeTypeUtil {

  private final static List<CedarNodeType> validRestTypesList;
  private final static List<String> validRestTypeValuesList;
  private final static Set<CedarNodeType> validRestTypesSet;
  private final static List<String> validVersioningTypeValuesList;

  static {
    validRestTypesSet = new LinkedHashSet<>();
    validRestTypesSet.add(CedarNodeType.FOLDER);
    validRestTypesSet.add(CedarNodeType.FIELD);
    validRestTypesSet.add(CedarNodeType.ELEMENT);
    validRestTypesSet.add(CedarNodeType.TEMPLATE);
    validRestTypesSet.add(CedarNodeType.INSTANCE);

    validRestTypesList = new ArrayList<>();
    validRestTypeValuesList = new ArrayList<>();
    for (CedarNodeType nt : validRestTypesSet) {
      validRestTypesList.add(nt);
      validRestTypeValuesList.add(nt.getValue());
    }

    validVersioningTypeValuesList = new ArrayList<>();
    validVersioningTypeValuesList.add(CedarNodeType.ELEMENT.getValue());
    validVersioningTypeValuesList.add(CedarNodeType.TEMPLATE.getValue());
  }

  public static List<String> getValidNodeTypeValuesForRestCalls() {
    return validRestTypeValuesList;
  }

  public static List<String> getValidNodeTypeValuesForVersioning() {
    return validVersioningTypeValuesList;
  }

  public static List<CedarNodeType> getValidNodeTypesForRestCalls() {
    return validRestTypesList;
  }

  public static boolean isValidForRestCall(CedarNodeType nodeType) {
    return validRestTypesSet.contains(nodeType);
  }
}
