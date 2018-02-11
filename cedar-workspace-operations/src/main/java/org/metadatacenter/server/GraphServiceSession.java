package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.FolderServerArc;

import java.util.List;

public interface GraphServiceSession {

  List<FolderServerArc> getOutgoingArcs(String nodeId);

  List<FolderServerArc> getIncomingArcs(String nodeId);

}
