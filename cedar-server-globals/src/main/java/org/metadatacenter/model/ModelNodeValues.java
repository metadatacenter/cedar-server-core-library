package org.metadatacenter.model;

public class ModelNodeValues {

  private ModelNodeValues() {
  }

  public static final String JSON_SCHEMA_IRI = "http://json-schema.org/draft-04/schema#";
  public static final String XSD_IRI = "http://www.w3.org/2001/XMLSchema#";
  public static final String PAV_IRI = "http://purl.org/pav/";
  public static final String SCHEMA_IRI = "http://schema.org/";
  public static final String OSLC_IRI = "http://open-services.net/ns/core#";
  public static final String BIBO_IRI = "http://purl.org/ontology/bibo/";

  public static final boolean TRUE = true;
  public static final boolean FALSE = false;

  public static final String XSD_STRING = "xsd:string";
  public static final String XSD_DATETIME = "xsd:dateTime";

  public static final String ONTOLOGY_CLASS = "OntologyClass";

  public static final String LD_ID = "@id";
  public static final String LD_TYPE = "@type";

  /*
   * Value set for JSON-Schema "type" property
   */
  public static final String OBJECT = "object";
  public static final String STRING = "string";
  public static final String INTEGER = "integer";
  public static final String NUMBER = "number";
  public static final String BOOLEAN = "boolean";
  public static final String ARRAY = "array";
  public static final String NULL = "null";

  /*
   * Value set for JSON-Schema "format" property
   */
  public static final String URI = "uri";
  public static final String DATE_TIME = "date-time";

  /*
   * Value set for CEDAR "inputType" property
   */
  public static final String TEXT_FIELD = "textfield";
  public static final String TEXT_AREA = "textarea";
  public static final String RADIO = "radio";
  public static final String DATE = "date";
  public static final String EMAIL = "email";
  public static final String LIST = "list";
  public static final String NUMERIC = "numeric";
  public static final String PHONE_NUMBER = "phone-number";
  public static final String LINK = "link";
  public static final String CHECKBOX = "checkbox";

  /*
 * Value set for CEDAR "dateType" property
 */
  public static final String SINGLE_DATE = "single-date";
  public static final String DATE_RANGE = "date-range";
}
