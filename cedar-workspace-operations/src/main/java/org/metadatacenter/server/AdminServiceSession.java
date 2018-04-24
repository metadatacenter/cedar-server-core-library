package org.metadatacenter.server;

import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public interface AdminServiceSession {

  void ensureGlobalObjectsExists();

  boolean wipeAllData();

  boolean createUniqueConstraint(NodeLabel nodeLabel, NodeProperty property);

  boolean createIndex(NodeLabel nodeLabel, NodeProperty property);

}
