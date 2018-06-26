package org.metadatacenter.util.http;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.config.PaginationConfig;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.exception.CedarAssertionException;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;
import org.metadatacenter.util.CedarNodeTypeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PagedSortedTypedQuery extends PagedSortedQuery {

  protected Optional<String> resourceTypesInput;
  protected List<CedarNodeType> nodeTypeList;
  protected Optional<String> versionInput;
  protected ResourceVersionFilter version;
  protected Optional<String> publicationStatusInput;
  protected ResourcePublicationStatusFilter publicationStatus;

  public PagedSortedTypedQuery(PaginationConfig config) {
    super(config);

    this.nodeTypeList = new ArrayList<>();
  }

  public PagedSortedTypedQuery resourceTypes(Optional<String> resourceTypesInput) {
    this.resourceTypesInput = resourceTypesInput;
    return this;
  }

  public PagedSortedTypedQuery version(Optional<String> versionInput) {
    this.versionInput = versionInput;
    return this;
  }

  public PagedSortedTypedQuery publicationStatus(Optional<String> publicationStatusInput) {
    this.publicationStatusInput = publicationStatusInput;
    return this;
  }

  @Override
  public PagedSortedTypedQuery sort(Optional<String> sortInput) {
    super.sort(sortInput);
    return this;
  }

  @Override
  public PagedSortedTypedQuery limit(Optional<Integer> limitInput) {
    super.limit(limitInput);
    return this;
  }

  @Override
  public PagedSortedTypedQuery offset(Optional<Integer> offsetInput) {
    super.offset(offsetInput);
    return this;
  }

  @Override
  public void validate() throws CedarException {
    validateLimit();
    validateOffset();
    validateSorting();
    validateResourceTypes();
    validateVersion();
    validatePublicationStatus();
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

  protected void validateResourceTypes() throws CedarException {
    String nodeTypesString = null;
    List<String> nodeTypeStringList;
    if (!resourceTypesInput.isPresent()) {
      nodeTypeStringList = CedarNodeTypeUtil.getValidNodeTypeValuesForRestCalls();
    } else {
      nodeTypesString = resourceTypesInput.get();
      if (nodeTypesString != null) {
        nodeTypesString = nodeTypesString.trim();
      }
      if (nodeTypesString == null || nodeTypesString.isEmpty()) {
        throw new CedarAssertionException("If present, 'resource_types' must be a comma separated list!")
            .badRequest()
            .parameter("resource_types", nodeTypesString);
      }
      nodeTypeStringList = Arrays.asList(StringUtils.split(nodeTypesString, ","));
    }
    nodeTypeList = new ArrayList<>();
    for (String rt : nodeTypeStringList) {
      CedarNodeType crt = CedarNodeType.forValue(rt);
      if (!CedarNodeTypeUtil.isValidForRestCall(crt)) {
        throw new CedarAssertionException("You passed an illegal 'resource_types':'" + rt + "'. The allowed values " +
            "are:" +
            CedarNodeTypeUtil.getValidNodeTypeValuesForRestCalls())
            .errorKey(CedarErrorKey.INVALID_NODE_TYPE)
            .badRequest()
            .parameter("resource_types", nodeTypesString)
            .parameter("invalidResourceTypes", rt)
            .parameter("allowedResourceTypes", CedarNodeTypeUtil.getValidNodeTypeValuesForRestCalls());
      } else {
        nodeTypeList.add(crt);
      }
    }
  }

  private void validateVersion() throws CedarException {
    if (!versionInput.isPresent()) {
      version = ResourceVersionFilter.ALL;
    } else {
      String versionString = versionInput.get();
      if (versionString == null || versionString.isEmpty()) {
        throw new CedarAssertionException("If present, 'version' must be one of the following:" +
            ResourceVersionFilter.getValidValues())
            .badRequest()
            .parameter("version", versionString);
      }
      version = ResourceVersionFilter.forValue(versionString);
      if (version == null) {
        throw new CedarAssertionException("Invalid value for 'version'! Must be one of the following:" +
            ResourceVersionFilter.getValidValues())
            .badRequest()
            .parameter("version", versionString);
      }
    }
  }

  private void validatePublicationStatus() throws CedarException {
    if (!publicationStatusInput.isPresent()) {
      publicationStatus = ResourcePublicationStatusFilter.ALL;
    } else {
      String publicationStatusString = publicationStatusInput.get();
      if (publicationStatusString == null || publicationStatusString.isEmpty()) {
        throw new CedarAssertionException("If present, 'publicationStatus' must be one of the following:" +
            ResourcePublicationStatusFilter.getValidValues())
            .badRequest()
            .parameter("publicationStatus", publicationStatusString);
      }
      publicationStatus = ResourcePublicationStatusFilter.forValue(publicationStatusString);
      if (publicationStatus == null) {
        throw new CedarAssertionException("Invalid value for 'publicationStatus'! Must be one of the following:" +
            ResourcePublicationStatusFilter.getValidValues())
            .badRequest()
            .parameter("publicationStatus", publicationStatusString);
      }
    }
  }

  public ResourceVersionFilter getVersion() {
    return version;
  }

  public ResourcePublicationStatusFilter getPublicationStatus() {
    return publicationStatus;
  }

  public String getVersionAsString() {
    return version != null ? version.getValue() : "";
  }

  public String getPublicationStatusAsString() {
    return publicationStatus != null ? publicationStatus.getValue() : "";
  }
}
