package org.metadatacenter.server.search.permission;

import org.metadatacenter.server.cache.util.CacheService;
import org.metadatacenter.server.search.SearchPermissionQueueEvent;
import org.metadatacenter.server.search.SearchPermissionQueueEventType;
import org.metadatacenter.server.search.IndexedDocumentId;

import static org.metadatacenter.server.search.SearchPermissionQueueEventType.*;

public class SearchPermissionEnqueueService {

  private final CacheService cacheService;

  public SearchPermissionEnqueueService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  private void enqueue(String id, SearchPermissionQueueEventType eventType, IndexedDocumentId parentId) {
    SearchPermissionQueueEvent event = new SearchPermissionQueueEvent(id, eventType, parentId);
    cacheService.enqueueEvent(event);
  }

  private void enqueue(String id, SearchPermissionQueueEventType eventType) {
    enqueue(id, eventType, null);
  }

  public void resourceCreated(String id, IndexedDocumentId parentId) {
    enqueue(id, RESOURCE_CREATED, parentId);
  }

  public void resourceMoved(String id) {
    enqueue(id, RESOURCE_MOVED);
  }

  public void resourcePermissionsChanged(String id) {
    // TODO: Check if this was a real change. Check this at the calling side
    enqueue(id, RESOURCE_PERMISSION_CHANGED);
  }

  public void folderCreated(String id, IndexedDocumentId parentId) {
    enqueue(id, FOLDER_CREATED, parentId);
  }

  public void folderMoved(String id) {
    enqueue(id, FOLDER_MOVED);
  }

  public void folderPermissionsChanged(String id) {
    // TODO: Check if this was a real change. Check this at the calling side
    enqueue(id, FOLDER_PERMISSION_CHANGED);
  }

  public void groupMembersUpdated(String id) {
    enqueue(id, GROUP_MEMBERS_UPDATED);
  }

  public void groupDeleted(String id) {
    enqueue(id, GROUP_DELETED);
  }

}
