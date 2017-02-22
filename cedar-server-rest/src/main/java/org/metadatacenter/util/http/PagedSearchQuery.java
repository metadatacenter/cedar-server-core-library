package org.metadatacenter.util.http;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.config.PaginationConfig;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.exception.CedarAssertionException;
import org.metadatacenter.util.CedarNodeTypeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PagedSearchQuery extends PagedSortedQuery {

  private Optional<String> resourceTypesInput;

  private List<CedarNodeType> nodeTypeList;

  public PagedSearchQuery(PaginationConfig config, Optional<String> resourceTypesInput, Optional<String> sortInput,
                          Optional<Integer> limitInput, Optional<Integer> offsetInput) {
    super(config, sortInput, limitInput, offsetInput);

    this.resourceTypesInput = resourceTypesInput;

    nodeTypeList = new ArrayList<>();
  }

  public void validate() throws CedarException {
    super.validate();

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

  public List<String> getNodeTypeAsStringList() {
    List<String> r = new ArrayList<>();
    for (CedarNodeType nt : nodeTypeList) {
      r.add(nt.getValue());
    }
    return r;
  }

  public String getNodeTypesAsString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < nodeTypeList.size(); i++) {
      sb.append(nodeTypeList.get(i).getValue());
      if (i != nodeTypeList.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }
}
