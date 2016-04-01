package org.metadatacenter.provenance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ProvenanceUtil {

  public static final String PAV_CREATED_ON = "pav:createdOn";
  public static final String PAV_CREATED_BY = "pav:createdBy";
  public static final String PAV_LAST_UPDATED_ON = "pav:lastUpdatedOn";
  public static final String CEDAR_LAST_UPDATED_BY = "cedar:lastUpdatedBy";


  private ProvenanceUtil() {
  }

  private static void setProvenanceInfo(JsonNode node, ProvenanceInfo pi, boolean justModification) {
    ObjectNode resource = (ObjectNode) node;
    if (!justModification) {
      resource.put(PAV_CREATED_ON, pi.getCreatedOn());
      resource.put(PAV_CREATED_BY, pi.getCreatedBy());
    }
    resource.put(PAV_LAST_UPDATED_ON, pi.getLastUpdatedOn());
    resource.put(CEDAR_LAST_UPDATED_BY, pi.getLastUpdatedBy());
  }

  public static void addProvenanceInfo(JsonNode node, ProvenanceInfo pi) {
    setProvenanceInfo(node, pi, false);
  }

  public static void patchProvenanceInfo(JsonNode node, ProvenanceInfo pi) {
    setProvenanceInfo(node, pi, true);
  }

}
