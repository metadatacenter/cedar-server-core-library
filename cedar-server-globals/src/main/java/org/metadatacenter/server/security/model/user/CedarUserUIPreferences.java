package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CedarUserUIFolderView.class, name = "CedarUserUIFolderView"),
    @JsonSubTypes.Type(value = CedarUserUIPopulateATemplate.class, name = "CedarUserUIPopulateATemplate"),
    @JsonSubTypes.Type(value = CedarUserUIResourceTypeFilters.class, name = "CedarUserUIResourceTypeFilters"),
    @JsonSubTypes.Type(value = CedarUserUIPreferencesMap.class, name = "CedarUserUIPreferencesMap"),
    @JsonSubTypes.Type(value = CedarUserUIFacetedSearch.class, name = "CedarUserUIFacetedSearch"),
    @JsonSubTypes.Type(value = CedarUserUIInfoPanel.class, name = "CedarUserUIInfoPanel")
})
public interface CedarUserUIPreferences {
}
