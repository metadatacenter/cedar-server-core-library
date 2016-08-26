package org.metadatacenter.util.provenance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceInfo;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.model.IAuthRequest;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.lang.String;import java.time.Instant;

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

  public static ProvenanceInfo buildFromUserURLId(String userURL) {
    ProvenanceInfo pi = new ProvenanceInfo();
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    pi.setCreatedOn(nowString);
    pi.setCreatedBy(userURL);
    pi.setLastUpdatedOn(nowString);
    pi.setLastUpdatedBy(userURL);
    return pi;
  }

  public static ProvenanceInfo build(CedarConfig cedarConfig, IAuthRequest authRequest) {
    String id = null;
    try {
      CedarUser accountInfo = Authorization.getUser(authRequest);
      id = accountInfo.getId();
    } catch (CedarAccessException e) {
      e.printStackTrace();
    }
    return buildFromUUID(cedarConfig, id);
  }

  public static ProvenanceInfo buildFromUUID(CedarConfig cedarConfig, String userUUID) {
    String userURL = cedarConfig.getLinkedDataPrefix(CedarNodeType.USER) + userUUID;
    return buildFromUserURLId(userURL);
  }

}
