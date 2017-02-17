package org.metadatacenter.util.http;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.exception.CedarAssertionException;
import org.metadatacenter.server.neo4j.FolderContentSortOptions;
import org.metadatacenter.util.CedarNodeTypeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PagedSearchQuery {

  private Optional<String> resourceTypesInput;
  private Optional<String> sortInput;
  private Optional<Integer> limitInput;
  private Optional<Integer> offsetInput;

  private List<CedarNodeType> nodeTypeList;
  private List<String> sortList;
  private int limit;
  private int offset;

  public PagedSearchQuery(Optional<String> resourceTypesInput, Optional<String> sortInput, Optional<Integer> limitInput,
                          Optional<Integer> offsetInput) {

    this.resourceTypesInput = resourceTypesInput;
    this.sortInput = sortInput;
    this.limitInput = limitInput;
    this.offsetInput = offsetInput;

    nodeTypeList = new ArrayList<>();
    sortList = new ArrayList<>();
  }

  public void validate() throws CedarException {
    // Test limit
    // TODO : read the default and max from config here
    int limitDefault = 50; // set default
    int limitMax = 100; // set max
    limit = limitDefault;
    if (limitInput.isPresent()) {
      limit = limitInput.get();
      if (limit <= 0) {
        throw new CedarAssertionException("You should specify a positive limit!")
            .parameter("limit", limit);
      } else if (limit > limitMax) {
        throw new CedarAssertionException("You should specify a limit smaller than " + limitMax + "!")
            .parameter("limit", limit);
      }
    }

    // Test offset
    offset = 0;
    if (offsetInput.isPresent()) {
      if (offsetInput.get() < 0) {
        throw new CedarAssertionException("You should specify a positive or zero offset!")
            .parameter("offset", offsetInput.get());
      }
      offset = offsetInput.get();
    }

    // Test sort
    String sortString;
    if (sortInput.isPresent()) {
      sortString = sortInput.get();
    } else {
      sortString = FolderContentSortOptions.getDefaultSortField().getName();
    }

    if (sortString != null) {
      sortString = sortString.trim();
    }

    sortList = Arrays.asList(StringUtils.split(sortString, ","));
    for (String s : sortList) {
      String test = s;
      if (s != null && s.startsWith("-")) {
        test = s.substring(1);
      }
      if (!FolderContentSortOptions.isKnownField(test)) {
        throw new CedarAssertionException("You passed an illegal sort type:'" + s + "'. The allowed values are:" +
            FolderContentSortOptions.getKnownFieldNames())
            .parameter("sort", s)
            .parameter("allowedSort", FolderContentSortOptions.getKnownFieldNames());
      }
    }

    // Test resourceTypes
    String nodeTypesString = null;
    if (resourceTypesInput.isPresent()) {
      nodeTypesString = resourceTypesInput.get();
    }
    if (nodeTypesString != null) {
      nodeTypesString = nodeTypesString.trim();
    }
    if (nodeTypesString == null || nodeTypesString.isEmpty()) {
      throw new CedarAssertionException("You must pass in resource_types as a comma separated list!")
          .parameter("resource_types", nodeTypesString);
    }

    List<String> nodeTypeStringList = Arrays.asList(StringUtils.split(nodeTypesString, ","));
    nodeTypeList = new ArrayList<>();
    for (String rt : nodeTypeStringList) {
      CedarNodeType crt = CedarNodeType.forValue(rt);
      if (!CedarNodeTypeUtil.isValidForRestCall(crt)) {
        throw new CedarAssertionException("You passed an illegal resource_types:'" + rt + "'. The allowed values are:" +
            CedarNodeTypeUtil.getValidNodeTypeValuesForRestCalls())
            .errorKey(CedarErrorKey.INVALID_NODE_TYPE)
            .parameter("resource_types", nodeTypesString)
            .parameter("invalidResourceTypes", rt)
            .parameter("allowedResourceTypes", CedarNodeTypeUtil.getValidNodeTypeValuesForRestCalls());
      } else {
        nodeTypeList.add(crt);
      }
    }
  }

  public List<CedarNodeType> getNodeTypeList() {
    return nodeTypeList;
  }

  public List<String> getSortList() {
    return sortList;
  }

  public String getSortListAsString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sortList.size(); i++) {
      sb.append(sortList.get(i));
      if (i != sortList.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public String getNodeTypesAsList() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nodeTypeList.size(); i++) {
      sb.append(nodeTypeList.get(i).getValue());
      if (i != nodeTypeList.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public int getLimit() {
    return limit;
  }

  public int getOffset() {
    return offset;
  }
}
