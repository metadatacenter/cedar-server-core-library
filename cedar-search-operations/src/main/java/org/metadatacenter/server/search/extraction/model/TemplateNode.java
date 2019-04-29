package org.metadatacenter.server.search.extraction.model;

import org.metadatacenter.model.CedarNodeType;

import java.util.List;
import java.util.Optional;

/**
 * Stores path and some basic characteristics of template elements and fields
 */
public class TemplateNode {

  private String id; // Resource identifier (i.e., @id field)
  private String name; // Json key
  private String prefLabel; // Resource name
  private List<String> path; // List of json keys from the root (it includes the key of the current node)
  private CedarNodeType type; // Node type (e.g. field)
  private Optional<String> instanceType; // Instance type. It is the type of the field defined using an ontology term
  private boolean isArray;

  public TemplateNode(String id, String name, String prefLabel, List<String> path, CedarNodeType type,
                      Optional<String> instanceType, boolean isArray) {
    this.id = id;
    this.name = name;
    this.prefLabel = prefLabel;
    this.path = path;
    this.type = type;
    this.instanceType = instanceType;
    this.isArray = isArray;
  }

  public String getId() { return id;}

  public String getName() { return name; }

  public String getPrefLabel() { return prefLabel; }

  public List<String> getPath() { return path;}

  public CedarNodeType getType() {
    return type;
  }

  public Optional<String> getInstanceType() { return instanceType; }

  public boolean isArray() {
    return isArray;
  }

  public String generatePathDotNotation() {
    return String.join(".", path);
  }

  public boolean isTemplateFieldNode() {
    if (type.equals(CedarNodeType.FIELD)) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean isTemplateElementNode() {
    if (type.equals(CedarNodeType.ELEMENT)) {
      return true;
    }
    else {
      return false;
    }
  }

}
