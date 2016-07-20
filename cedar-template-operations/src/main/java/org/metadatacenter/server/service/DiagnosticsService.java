package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface DiagnosticsService<T> {

  @NonNull
  public T heartbeat();
}
