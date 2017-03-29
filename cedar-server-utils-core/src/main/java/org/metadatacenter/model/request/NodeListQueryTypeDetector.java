package org.metadatacenter.model.request;

import java.util.Optional;

public class NodeListQueryTypeDetector {
  public static NodeListQueryType detect(Optional<String> q, Optional<String> derivedFromId, Optional<String> sharing) {

    if ((q == null || !q.isPresent() || q.get().isEmpty() || "*".equals(q.get())) &&
        (derivedFromId == null || !derivedFromId.isPresent() || derivedFromId.get().isEmpty()) &&
        (sharing == null || !sharing.isPresent() || sharing.get().isEmpty())) {
      return NodeListQueryType.VIEW_ALL;
    }

    if (q != null && q.isPresent() && !q.get().isEmpty()) {
      return NodeListQueryType.SEARCH_TERM;
    }

    if (derivedFromId != null && derivedFromId.isPresent() && !derivedFromId.get().isEmpty()) {
      return NodeListQueryType.SEARCH_DERIVED_FROM;
    }

    if (sharing != null && sharing.isPresent() && !sharing.get().isEmpty()) {
      if ("shared-with-me".equals(sharing.get())) {
        return NodeListQueryType.VIEW_SHARED_WITH_ME;
      }
    }
    return NodeListQueryType.UNKNOWN;
  }
}
