package org.metadatacenter.server;

public interface AdminServiceSession {
  void ensureGlobalObjectsExists();

  boolean wipeAllData();
}
