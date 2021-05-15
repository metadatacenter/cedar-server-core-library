package org.metadatacenter.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonDeserialize(converter = MyClassSanitizer.class)
public class TrustedFoldersConfig {

  private String foldersStr;

  @JsonIgnore
  private Map<String, String> foldersMap;

  public String getFoldersStr() {
    return foldersStr;
  }

  public Map<String, String> getFoldersMap() {
    return foldersMap;
  }

  public void setFoldersMap(Map<String, String> foldersMap) {
    this.foldersMap = foldersMap;
  }

}

/**
 * This class transforms the input string to a map to be able to quickly check if a folderId is trusted
 */
class MyClassSanitizer extends StdConverter<TrustedFoldersConfig, TrustedFoldersConfig> {
  @Override
  public TrustedFoldersConfig convert(TrustedFoldersConfig trustedFoldersConfig) {
    ObjectMapper mapper = new ObjectMapper();

    try {
      Map<String, List<String>> map = mapper.readValue(trustedFoldersConfig.getFoldersStr(), Map.class);
      Map<String, String> folderToEntityMap = new HashMap<>();
      for (String entityName : map.keySet()) {
        for (String folderId : map.get(entityName)) {
          folderToEntityMap.put(folderId, entityName);
        }
      }
      trustedFoldersConfig.setFoldersMap(folderToEntityMap);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return trustedFoldersConfig;
  }
}
