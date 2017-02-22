package org.metadatacenter.util.http;

import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.config.PaginationConfig;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.exception.CedarAssertionException;
import org.metadatacenter.server.folder.QuerySortOptions;
import org.metadatacenter.util.CedarNodeTypeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PagedSortedQuery extends PagedQuery {

  private Optional<String> sortInput;

  private List<String> sortList;

  public PagedSortedQuery(PaginationConfig config, Optional<String> sortInput,
                          Optional<Integer> limitInput, Optional<Integer> offsetInput) {
    super(config, limitInput, offsetInput);
    this.sortInput = sortInput;

    sortList = new ArrayList<>();
  }

  public void validate() throws CedarException {
    super.validate();

    String sortString;
    if (sortInput.isPresent()) {
      sortString = sortInput.get();
    } else {
      sortString = QuerySortOptions.getDefaultSortField().getName();
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
      if (!QuerySortOptions.isKnownField(test)) {
        throw new CedarAssertionException("You passed an illegal sort type:'" + s + "'. The allowed values are:" +
            QuerySortOptions.getKnownFieldNames())
            .parameter("sort", s)
            .parameter("allowedSort", QuerySortOptions.getKnownFieldNames());
      }
    }

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
}
