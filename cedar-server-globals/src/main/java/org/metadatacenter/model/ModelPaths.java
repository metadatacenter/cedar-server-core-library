package org.metadatacenter.model;

public class ModelPaths {

  private ModelPaths() {
  }

  public static final String SCHEMA_DESCRIPTION = "/schema:description";
  public static final String SCHEMA_NAME = "/schema:name";
  public static final String SCHEMA_IS_BASED_ON = "/" + ModelNodeNames.SCHEMA_IS_BASED_ON;

  public static final String CREATED_BY = "/createdBy";
  public static final String PAV_CREATED_BY = "/pav:createdBy";
  public static final String PAV_CREATED_ON = "/pav:createdOn";

  public static final String LAST_UPDATED_BY = "/lastUpdatedBy";
  public static final String OSLC_MODIFIED_BY = "/oslc:modifiedBy";
  public static final String PAV_LAST_UPDATED_ON = "/pav:lastUpdatedOn";

  public static final String PAV_VERSION = "/" + ModelNodeNames.PAV_VERSION;
  public static final String BIBO_STATUS = "/" + ModelNodeNames.BIBO_STATUS;

  public static final String AT_ID = "/@id";
}
