package org.metadatacenter.model;

public class ModelPaths {

  private ModelPaths() {
  }

  public static final String SCHEMA_DESCRIPTION =  "/" + ModelNodeNames.SCHEMA_ORG_DESCRIPTION;
  public static final String SCHEMA_NAME = "/" + ModelNodeNames.SCHEMA_ORG_NAME;
  public static final String SCHEMA_IDENTIFIER =  "/" + ModelNodeNames.SCHEMA_ORG_IDENTIFIER;
  public static final String SCHEMA_IS_BASED_ON = "/" + ModelNodeNames.SCHEMA_IS_BASED_ON;

  public static final String CREATED_BY = "/createdBy";
  public static final String PAV_CREATED_BY = "/" + ModelNodeNames.PAV_CREATED_BY;
  public static final String PAV_CREATED_ON = "/" + ModelNodeNames.PAV_CREATED_ON;

  public static final String LAST_UPDATED_BY = "/lastUpdatedBy";
  public static final String OSLC_MODIFIED_BY = "/" + ModelNodeNames.OSLC_MODIFIED_BY;
  public static final String PAV_LAST_UPDATED_ON = "/" + ModelNodeNames.PAV_LAST_UPDATED_ON;

  public static final String PAV_VERSION = "/" + ModelNodeNames.PAV_VERSION;
  public static final String BIBO_STATUS = "/" + ModelNodeNames.BIBO_STATUS;

  public static final String AT_ID = "/" + ModelNodeNames.JSON_LD_ID;
}
