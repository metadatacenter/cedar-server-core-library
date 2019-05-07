package org.metadatacenter.server;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.FolderServerArc;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;

import java.util.List;

public interface GraphServiceSession {

  List<FolderServerArc> getOutgoingArcs(String nodeId);

  List<FolderServerArc> getIncomingArcs(String nodeId);

  FolderServerUser createUser(JsonNode node);

  FolderServerGroup createGroup(JsonNode node);

  FileSystemResource createNode(JsonNode node);

  boolean createArc(String sourceId, RelationLabel relationLabel, String targetId);
}
