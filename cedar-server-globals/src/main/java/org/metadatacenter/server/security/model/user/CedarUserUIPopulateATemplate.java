package org.metadatacenter.server.security.model.user;

public class CedarUserUIPopulateATemplate implements CedarUserUIPreferences {

  private boolean opened;
  private String sortBy;
  private SortDirection sortDirection;
  private ViewMode viewMode;

  public CedarUserUIPopulateATemplate() {
  }

  public boolean isOpened() {
    return opened;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  public ViewMode getViewMode() {
    return viewMode;
  }

  public void setViewMode(ViewMode viewMode) {
    this.viewMode = viewMode;
  }
}
