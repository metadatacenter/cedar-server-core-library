package org.metadatacenter.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelNodeNames
{

  private ModelNodeNames()
  {
  }

  /*
   * JSON-LD Keywords
   */
  public static final String LD_CONTEXT = "@context";
  public static final String LD_ID = "@id";
  public static final String LD_TYPE = "@type";
  public static final String LD_VALUE = "@value";
  public static final String LD_GRAPH = "@graph";
  public static final String LD_BASE = "@base";
  public static final String LD_CONTAINER = "@container";
  public static final String LD_INDEX = "@index";
  public static final String LD_LANGUAGE = "@language";
  public static final String LD_LIST = "@list";
  public static final String LD_NEST = "@nest";
  public static final String LD_NONE = "@none";
  public static final String LD_PREFIX = "@prefix";
  public static final String LD_REVERSE = "@reverse";
  public static final String LD_VERSION = "@version";
  public static final String LD_VOCAB = "@vocab";

  public static final Set<String> JSON_LD_KEYWORDS = Stream
    .of(LD_CONTEXT, LD_ID, LD_TYPE, LD_VALUE, LD_GRAPH, LD_BASE, LD_CONTAINER, LD_INDEX, LD_LANGUAGE, LD_LIST, LD_NEST,
      LD_NONE, LD_PREFIX, LD_REVERSE, LD_VERSION, LD_VOCAB).collect(Collectors.toUnmodifiableSet());

  /*
   * JSON Schema Keywords
   */
  public static final String _SCHEMA = "$schema";
  public static final String _REF = "$ref";
  public static final String TYPE = "type";
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
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

  public static final Set<String> JSON_SCHEMA_KEYWORDS = Stream
    .of(_SCHEMA, _REF, TYPE, TITLE, DESCRIPTION, PROPERTIES, FORMAT, ENUM, ONE_OF, ITEMS, UNIQUE_ITEMS, MIN_ITEMS,
      MAX_ITEMS, MIN_LENGTH, MAX_LENGTH, MINIMUM, REQUIRED, PATTERN_PROPERTIES, ADDITIONAL_PROPERTIES)
    .collect(Collectors.toUnmodifiableSet());

  /*
   * CEDAR Artifact Keywords
   */

  // CEDAR model keywords that can occur at the top level of all (schema and instance) artifacts
  public static final String SCHEMA_NAME = "schema:name";
  public static final String SCHEMA_DESCRIPTION = "schema:description";
  public static final String SCHEMA_IDENTIFIER = "schema:identifier";
  public static final String PAV_CREATED_ON = "pav:createdOn";
  public static final String PAV_CREATED_BY = "pav:createdBy";
  public static final String PAV_LAST_UPDATED_ON = "pav:lastUpdatedOn";
  public static final String PAV_DERIVED_FROM = "pav:derivedFrom";
  public static final String OSLC_MODIFIED_BY = "oslc:modifiedBy";

  public static final Set<String> ARTIFACT_KEYWORDS = Stream.concat(JSON_LD_KEYWORDS.stream(), Stream
    .of(SCHEMA_NAME, SCHEMA_DESCRIPTION, SCHEMA_IDENTIFIER, PAV_CREATED_ON, PAV_CREATED_BY, PAV_LAST_UPDATED_ON,
      OSLC_MODIFIED_BY)).collect(Collectors.toUnmodifiableSet());

  // CEDAR model keywords that can occur at the top level of all schema artifacts
  public static final String SCHEMA_SCHEMA_VERSION = "schema:schemaVersion";
  public static final String PAV_VERSION = "pav:version";
  public static final String PAV_PREVIOUS_VERSION = "pav:previousVersion";
  public static final String BIBO_STATUS = "bibo:status";
  public static final String UI = "_ui";

  public static final Set<String> SCHEMA_ARTIFACT_KEYWORDS = Stream
    .concat(Stream.concat(ARTIFACT_KEYWORDS.stream(), JSON_SCHEMA_KEYWORDS.stream()),
      Stream.of(SCHEMA_SCHEMA_VERSION, PAV_VERSION, PAV_PREVIOUS_VERSION, BIBO_STATUS, UI))
    .collect(Collectors.toUnmodifiableSet());

  public static final Set<String> TEMPLATE_SCHEMA_ARTIFACT_KEYWORDS = SCHEMA_ARTIFACT_KEYWORDS;

  public static final Set<String> ELEMENT_SCHEMA_ARTIFACT_KEYWORDS = SCHEMA_ARTIFACT_KEYWORDS;

  // CEDAR model keywords that can occur at the top level of field schema artifacts
  public static final String VALUE_CONSTRAINTS = "_valueConstraints";

  public static final Set<String> FIELD_SCHEMA_ARTIFACT_KEYWORDS = Stream
    .concat(SCHEMA_ARTIFACT_KEYWORDS.stream(), Stream.of(VALUE_CONSTRAINTS)).collect(Collectors.toUnmodifiableSet());

  public static final Set<String> INSTANCE_ARTIFACT_KEYWORDS = ARTIFACT_KEYWORDS;

  // CEDAR model keywords that can occur at the top level of template instance artifacts
  public static final String SCHEMA_IS_BASED_ON = "schema:isBasedOn";

  public static final Set<String> TEMPLATE_INSTANCE_ARTIFACT_KEYWORDS = Stream
    .concat(INSTANCE_ARTIFACT_KEYWORDS.stream(), Stream.of(SCHEMA_IS_BASED_ON)).collect(Collectors.toUnmodifiableSet());

  public static final Set<String> ELEMENT_INSTANCE_ARTIFACT_KEYWORDS = INSTANCE_ARTIFACT_KEYWORDS;

  // Keywords that can occur in a field instance artifact
  public static final String RDFS_LABEL = "rdfs:label";
  public static final String SKOS_NOTATION = "skos:notation";
  public static final String SKOS_PREFLABEL = "skos:prefLabel";
  public static final String SKOS_ALTLABEL = "skos:altLabel";

  public static final Set<String> FIELD_INSTANCE_ARTIFACT_KEYWORDS = Stream.concat(INSTANCE_ARTIFACT_KEYWORDS.stream(),
    Stream.of(LD_ID, LD_VALUE, RDFS_LABEL, SKOS_NOTATION, SKOS_PREFLABEL, SKOS_ALTLABEL))
    .collect(Collectors.toUnmodifiableSet());

  // CEDAR keywords that can occur in a template and element schema artifact's _UI object;
  public static final String PAGES = "pages";
  public static final String PROPERTY_LABELS = "propertyLabels";
  public static final String PROPERTY_DESCRIPTIONS = "propertyDescriptions";
  public static final String ORDER = "order";

  public static final Set<String> TEMPLATE_SCHEMA_UI_KEYWORDS = Stream
    .of(PAGES, ORDER, PROPERTY_LABELS, PROPERTY_DESCRIPTIONS).collect(Collectors.toUnmodifiableSet());

  public static final Set<String> ELEMENT_SCHEMA_UI_KEYWORDS = Stream.of(ORDER, PROPERTY_LABELS, PROPERTY_DESCRIPTIONS)
    .collect(Collectors.toUnmodifiableSet());

  public static final String VALUE_RECOMMENDATION_ENABLED = "valueRecommendationEnabled";
  public static final String INPUT_TYPE = "inputType";
  public static final String HIDDEN = "hidden";

  public static final Set<String> FIELD_SCHEMA_UI_KEYWORDS = Stream.of(VALUE_RECOMMENDATION_ENABLED, INPUT_TYPE, HIDDEN)
    .collect(Collectors.toUnmodifiableSet());

  // CEDAR field input types
  public static final String TEXTFIELD_INPUT_TYPE = "textfield";
  public static final String TEXTAREA_INPUT_TYPE = "textarea";
  public static final String RADIO_INPUT_TYPE = "radio";
  public static final String CHECKBOX_INPUT_TYPE = "checkbox";
  public static final String DATE_INPUT_TYPE = "date";
  public static final String EMAIL_INPUT_TYPE = "email";
  public static final String LIST_INPUT_TYPE = "list";
  public static final String NUMERIC_INPUT_TYPE = "numeric";
  public static final String PHONE_NUMBER_INPUT_TYPE = "phone-number";
  public static final String SECTION_BREAK_INPUT_TYPE = "section-break";
  public static final String RICH_TEXT_INPUT_TYPE = "richtext";
  public static final String IMAGE_INPUT_TYPE = "image";
  public static final String LINK_INPUT_TYPE = "link";
  public static final String YOUTUBE_INPUT_TYPE = "youtube";

  public static final Set<String> INPUT_TYPES = Stream
    .of(TEXTFIELD_INPUT_TYPE, TEXTAREA_INPUT_TYPE, RADIO_INPUT_TYPE, CHECKBOX_INPUT_TYPE, DATE_INPUT_TYPE,
      EMAIL_INPUT_TYPE, LIST_INPUT_TYPE, NUMERIC_INPUT_TYPE, PHONE_NUMBER_INPUT_TYPE, SECTION_BREAK_INPUT_TYPE,
      RICH_TEXT_INPUT_TYPE, IMAGE_INPUT_TYPE, LINK_INPUT_TYPE, YOUTUBE_INPUT_TYPE)
    .collect(Collectors.toUnmodifiableSet());

  // CEDAR keywords that can occur in a field schema artifact's _valueConstraints object
  public static final String DATE_TYPE = "dateType";
  public static final String ONTOLOGIES = "ontologies";
  public static final String VALUE_SETS = "valueSets";
  public static final String CLASSES = "classes";
  public static final String BRANCHES = "branches";
  public static final String LITERALS = "literals";
  public static final String MULTIPLE_CHOICE = "multipleChoice";
  public static final String DEFAULT_VALUE = "defaultValue";
  public static final String URI = "uri";
  public static final String NUM_TERMS = "numTerms";
  public static final String MAX_DEPTH = "maxDepth";
  public static final String VS_COLLECTION = "vsCollection";
  public static final String SOURCE = "source";
  public static final String EXCLUSIONS = "exclusions";
  public static final String REQUIRED_VALUE = "requiredValue";
  public static final String SELECTED_BY_DEFAULT = "selectedByDefault";
  public static final String MIN_STRING_LENGTH = "minLength";
  public static final String MAX_STRING_LENGTH = "maxLength";
  public static final String MIN_NUMBER_VALUE = "minValue";
  public static final String MAX_NUMBER_VALUE = "maxValue";
  public static final String DECIMAL_PLACE = "decimalPlace";
  public static final String NUMBER_TYPE = "numberType";
  public static final String UNIT_OF_MEASURE = "unitOfMeasure";

  public static final Set<String> VALUE_CONSTRAINTS_KEYWORDS = Stream
    .of(DATE_TYPE, ONTOLOGIES, VALUE_SETS, CLASSES, BRANCHES, LITERALS, MULTIPLE_CHOICE, DEFAULT_VALUE, URI, NUM_TERMS,
      MAX_DEPTH, VS_COLLECTION, SOURCE, EXCLUSIONS, REQUIRED_VALUE, SELECTED_BY_DEFAULT, MIN_STRING_LENGTH,
      MAX_STRING_LENGTH, MIN_NUMBER_VALUE, MAX_NUMBER_VALUE, DECIMAL_PLACE, NUMBER_TYPE, UNIT_OF_MEASURE)
    .collect(Collectors.toUnmodifiableSet());

  public static final String XSD = "xsd";
  public static final String PAV = "pav";
  public static final String SCHEMA = "schema";
  public static final String OSLC = "oslc";
  public static final String BIBO = "bibo";
  public static final String SKOS = "skos";

  public static final Set<String> ARTIFACT_CONTEXT_PREFIXES = Stream.of(XSD, PAV, SCHEMA, OSLC, BIBO, SKOS)
    .collect(Collectors.toUnmodifiableSet());

  public static final String TEMPLATE_SCHEMA_ARTIFACT_TYPE_IRI = "https://schema.metadatacenter.org/core/Template";
  public static final String ELEMENT_SCHEMA_ARTIFACT_TYPE_IRI = "https://schema.metadatacenter.org/core/TemplateElement";
  public static final String FIELD_SCHEMA_ARTIFACT_TYPE_IRI = "https://schema.metadatacenter.org/core/TemplateField";

  public static final Set<String> SCHEMA_ARTIFACT_TYPE_IRIS = Stream
    .of(TEMPLATE_SCHEMA_ARTIFACT_TYPE_IRI, ELEMENT_SCHEMA_ARTIFACT_TYPE_IRI, FIELD_SCHEMA_ARTIFACT_TYPE_IRI)
    .collect(Collectors.toUnmodifiableSet());
}
