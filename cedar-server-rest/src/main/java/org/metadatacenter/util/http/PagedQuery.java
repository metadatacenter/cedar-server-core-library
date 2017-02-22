package org.metadatacenter.util.http;

import org.metadatacenter.config.PaginationConfig;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.rest.exception.CedarAssertionException;

import java.util.Optional;

public class PagedQuery {

  private Optional<Integer> limitInput;
  private Optional<Integer> offsetInput;

  private PaginationConfig config;
  private int limit;
  private int offset;


  public PagedQuery(PaginationConfig config, Optional<Integer> limitInput, Optional<Integer> offsetInput) {
    this.config = config;
    this.limitInput = limitInput;
    this.offsetInput = offsetInput;
  }

  public void validate() throws CedarException {
    int limitDefault = config.getDefaultPageSize();
    int limitMax = config.getMaxPageSize();
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

    offset = 0;
    if (offsetInput.isPresent()) {
      if (offsetInput.get() < 0) {
        throw new CedarAssertionException("You should specify a positive or zero offset!")
            .parameter("offset", offsetInput.get());
      }
      offset = offsetInput.get();
    }
  }

  public int getLimit() {
    return limit;
  }

  public int getOffset() {
    return offset;
  }
}
