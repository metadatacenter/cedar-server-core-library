package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerCategoryListResponse {

  private List<FolderServerCategory> categories;

  public FolderServerCategoryListResponse() {
    this.categories = new ArrayList<>();
  }

  public List<FolderServerCategory> getCategories() {
    return categories;
  }

  public void setCategories(List<FolderServerCategory> groups) {
    this.categories = categories;
  }
}
