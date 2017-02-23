package org.metadatacenter.util.provenance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.model.provenance.ProvenanceInfo;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.model.AuthRequest;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class ProvenanceUtil {

  public static final String PAV_CREATED_ON = "pav:createdOn";
  public static final String PAV_CREATED_BY = "pav:createdBy";
  public static final String PAV_LAST_UPDATED_ON = "pav:lastUpdatedOn";
  public static final String LAST_UPDATED_BY = "oslc:modifiedBy";

  private static final Logger log = LoggerFactory.getLogger(ProvenanceUtil.class);

  private LinkedDataUtil linkedDataUtil;

  public ProvenanceUtil(LinkedDataUtil linkedDataUtil) {
    this.linkedDataUtil = linkedDataUtil;
    this.linkedDataUtil = linkedDataUtil;
  }

  private void setProvenanceInfo(JsonNode node, ProvenanceInfo pi, boolean justModification) {
    ObjectNode resource = (ObjectNode) node;
    if (!justModification) {
      resource.put(PAV_CREATED_ON, pi.getCreatedOn());
      resource.put(PAV_CREATED_BY, pi.getCreatedBy());
    }
    resource.put(PAV_LAST_UPDATED_ON, pi.getLastUpdatedOn());
    resource.put(LAST_UPDATED_BY, pi.getLastUpdatedBy());
  }

  public void addProvenanceInfo(JsonNode node, ProvenanceInfo pi) {
    setProvenanceInfo(node, pi, false);
  }

  public void patchProvenanceInfo(JsonNode node, ProvenanceInfo pi) {
    setProvenanceInfo(node, pi, true);
  }

  public ProvenanceInfo buildFromUserURLId(String userURL) {
    ProvenanceInfo pi = new ProvenanceInfo();
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    pi.setCreatedOn(nowString);
    pi.setCreatedBy(userURL);
    pi.setLastUpdatedOn(nowString);
    pi.setLastUpdatedBy(userURL);
    return pi;
  }

  public ProvenanceInfo build(AuthRequest authRequest) {
    String id = null;
    try {
      CedarUser accountInfo = Authorization.getUser(authRequest);
      id = accountInfo.getId();
    } catch (CedarAccessException e) {
      log.error("There was an error building the provenance info", e);
    }
    return buildFromUUID(id);
  }

  public ProvenanceInfo build(CedarUser cu) {
    String id = cu.getId();
    return buildFromUUID(id);
  }

  public ProvenanceInfo buildFromUUID(String userUUID) {
    return buildFromUserURLId(linkedDataUtil.getLinkedDataId(CedarNodeType.USER, userUUID));
  }

}
