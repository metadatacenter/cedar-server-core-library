package org.metadatacenter.model;

public class ModelNodeNames {

  private ModelNodeNames() {
  }

  /*
   * JSON Schema Keywords
   */
  public static final String _SCHEMA = "$schema";
  public static final String _REF = "$ref";

  public static final String TYPE = "type";
  public static final String PROPERTIES = "properties";
  public static final String FORMAT = "format";
  public static final String ENUM = "enum";
  public static final String ONE_OF = "oneOf";
  public static final String ITEMS = "items";
  public static final String UNIQUE_ITEMS = "uniqueItems";
  public static final String MIN_ITEMS = "minItems";
  public static final String MAX_ITEMS = "maxItems";
  public static final String MIN_LENGTH = "minLength";
  public static final String MAX_LENGTH = "maxLength";
  public static final String MINIMUM = "minimum";
  public static final String REQUIRED = "required";
  public static final String PATTERN_PROPERTIES = "patternProperties";
  public static final String ADDITIONAL_PROPERTIES = "additionalProperties";

  /*
   * CEDAR Model Properties
   */
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
  public static final String INPUT_TYPE = "inputType";
  public static final String DATE_TYPE = "dateType";
  public static final String VALUE_RECOMMENDATION_ENABLED = "valueRecommendationEnabled";
  public static final String HIDDEN = "hidden";
  public static final String ONTOLOGIES = "ontologies";
  public static final String VALUE_SETS = "valueSets";
  public static final String CLASSES = "classes";
  public static final String BRANCHES = "branches";
  public static final String LITERALS = "literals";
  public static final String MULTIPLE_CHOICE = "multipleChoice";
  public static final String DEFAULT_VALUE = "defaultValue";
  public static final String URI = "uri";
  public static final String ACRONYM = "acronym";
  public static final String NAME = "name";
  public static final String LABEL = "label";
  public static final String PREF_LABEL = "prefLabel";
  public static final String NUM_TERMS = "numTerms";
  public static final String MAX_DEPTH = "maxDepth";
  public static final String VS_COLLECTION = "vsCollection";
  public static final String SOURCE = "source";
  public static final String EXCLUSIONS = "exclusions";
  public static final String REQUIRED_VALUE = "requiredValue";
  public static final String SELECTED_BY_DEFAULT = "selectedByDefault";
  public static final String UI = "_ui";
  public static final String VALUE_CONSTRAINTS = "_valueConstraints";
  public static final String MIN_STRING_LENGTH = "minLength";
  public static final String MAX_STRING_LENGTH = "maxLength";
  public static final String MIN_NUMBER_VALUE = "minValue";
  public static final String MAX_NUMBER_VALUE = "maxValue";
  public static final String DECIMAL_PLACE = "decimalPlace";
  public static final String NUMBER_TYPE = "numberType";
  public static final String UNIT_OF_MEASURE = "unitOfMeasure";

  public static final String XSD = "xsd";
  public static final String PAV = "pav";
  public static final String SCHEMA = "schema";
  public static final String OSLC = "oslc";
  public static final String BIBO = "bibo";
  public static final String SKOS = "skos";

  public static final String RDFS_LABEL = "rdfs:label";
  public static final String SCHEMA_DESCRIPTION = "schema:description";
  public static final String SCHEMA_NAME = "schema:name";
  public static final String SCHEMA_IS_BASED_ON = "schema:isBasedOn";
  public static final String SCHEMA_SCHEMA_VERSION = "schema:schemaVersion";
  public static final String PAV_CREATED_ON = "pav:createdOn";
  public static final String PAV_CREATED_BY = "pav:createdBy";
  public static final String PAV_LAST_UPDATED_ON = "pav:lastUpdatedOn";
  public static final String PAV_VERSION = "pav:version";
  public static final String PAV_PREVIOUS_VERSION = "pav:previousVersion";
  public static final String PAV_DERIVED_FROM = "pav:derivedFrom";
  public static final String OSLC_MODIFIED_BY = "oslc:modifiedBy";
  public static final String BIBO_STATUS = "bibo:status";
  public static final String SKOS_NOTATION = "skos:notation";
  public static final String SKOS_PREFLABEL = "skos:prefLabel";
  public static final String SKOS_ALTLABEL = "skos:altLabel";

  /*
   * JSON-LD Keywords
   */
  public static final String LD_CONTEXT = "@context";
  public static final String LD_ID = "@id";
  public static final String LD_TYPE = "@type";
  public static final String LD_VALUE = "@value";
}
