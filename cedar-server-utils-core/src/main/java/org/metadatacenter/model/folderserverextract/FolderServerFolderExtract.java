package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerFolderExtract extends FolderServerNodeExtract {

  public FolderServerFolderExtract() {
    super(CedarNodeType.FOLDER);
  }

  public static FolderServerFolderExtract fromFolder(FolderServerFolder folder) {
    try {
      return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(folder), FolderServerFolderExtract.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
