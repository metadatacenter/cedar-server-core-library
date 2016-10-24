package org.metadatacenter.server;

/**
 * Created by egyedia on 10/24/16.
 */
public interface AdminServiceSession {
  void ensureGlobalObjectsExists();

  boolean wipeAllData();
}
