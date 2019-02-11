package org.metadatacenter.model.request;

import java.util.Optional;

public class NodeListQueryTypeDetector {
  public static NodeListQueryType detect(Optional<String> q, Optional<String> id, Optional<String> isBasedOn,
                                         Optional<String> sharing) {

    if (id != null && id.isPresent() && !id.get().isEmpty()) {
      return NodeListQueryType.SEARCH_ID;
    }

    if ((q == null || !q.isPresent() || q.get().isEmpty() || "*".equals(q.get())) &&
        (isBasedOn == null || !isBasedOn.isPresent() || isBasedOn.get().isEmpty()) &&
        (sharing == null || !sharing.isPresent() || sharing.get().isEmpty())) {
      return NodeListQueryType.VIEW_ALL;
    }

    if (q != null && q.isPresent() && !q.get().isEmpty()) {
      return NodeListQueryType.SEARCH_TERM;
    }

    if (isBasedOn != null && isBasedOn.isPresent() && !isBasedOn.get().isEmpty()) {
      return NodeListQueryType.SEARCH_IS_BASED_ON;
    }

    if (sharing != null && sharing.isPresent() && !sharing.get().isEmpty()) {
      if ("shared-with-me".equals(sharing.get())) {
        return NodeListQueryType.VIEW_SHARED_WITH_ME;
      } else if ("shared-with-everybody".equals(sharing.get())) {
        return NodeListQueryType.VIEW_SHARED_WITH_EVERYBODY;
      }
    }
    return NodeListQueryType.UNKNOWN;
  }
}
