package org.metadatacenter.server.permissions;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.outcome.OutcomeWithReason;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.VersionServiceSession;
import org.metadatacenter.server.security.model.NodeWithIsBasedOn;
import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;

public class CurrentUserPermissionUpdaterForWorkspaceResource extends CurrentUserPermissionUpdater {

  private final PermissionServiceSession permissionSession;
  private final VersionServiceSession versionSession;
  private final CedarConfig cedarConfig;
  private final ResourceWithCurrentUserPermissions resource;

  private CurrentUserPermissionUpdaterForWorkspaceResource(PermissionServiceSession permissionSession,
                                                           VersionServiceSession versionSession,
                                                           CedarConfig cedarConfig,
                                                           ResourceWithCurrentUserPermissions resource) {
    this.permissionSession = permissionSession;
    this.versionSession = versionSession;
    this.cedarConfig = cedarConfig;
    this.resource = resource;
  }

  public static CurrentUserPermissionUpdater get(PermissionServiceSession permissionSession,
                                                 VersionServiceSession versionSession,
                                                 CedarConfig cedarConfig, ResourceWithCurrentUserPermissions resource) {
    return new CurrentUserPermissionUpdaterForWorkspaceResource(permissionSession, versionSession, cedarConfig,
        resource);
  }

  @Override
  public void update(CurrentUserPermissions currentUserPermissions) {
    String id = resource.getId();
    if (permissionSession.userHasWriteAccessToResource(id)) {
      currentUserPermissions.setCanWrite(true);
      currentUserPermissions.setCanDelete(true);
      currentUserPermissions.setCanRead(true);
      currentUserPermissions.setCanShare(true);
    } else if (permissionSession.userHasReadAccessToResource(id)) {
      currentUserPermissions.setCanRead(true);
    }
    if (permissionSession.userCanChangeOwnerOfResource(id)) {
      currentUserPermissions.setCanChangeOwner(true);
    }
    OutcomeWithReason versioningOutcome = versionSession.userCanPerformVersioning(resource);
    if (versioningOutcome.isNegative()) {
      currentUserPermissions.setCreateDraftErrorKey(versioningOutcome.getReason());
      currentUserPermissions.setPublishErrorKey(versioningOutcome.getReason());
    } else {
      OutcomeWithReason publishOutcome = versionSession.resourceCanBePublished(resource);
      if (publishOutcome.isPositive()) {
        currentUserPermissions.setCanPublish(true);
      } else {
        currentUserPermissions.setPublishErrorKey(publishOutcome.getReason());
      }
      OutcomeWithReason createDraftOutcome = versionSession.resourceCanBeDrafted(resource);
      if (createDraftOutcome.isPositive()) {
        currentUserPermissions.setCanCreateDraft(true);
      } else {
        currentUserPermissions.setCreateDraftErrorKey(createDraftOutcome.getReason());
      }
    }
    if (resource.getType() == CedarNodeType.TEMPLATE) {
      currentUserPermissions.setCanPopulate(true);
    }
    if (resource.getType() == CedarNodeType.INSTANCE) {
      NodeWithIsBasedOn instance = (NodeWithIsBasedOn) resource;
      ResourceUri basedOnTemplate = instance.getIsBasedOn();
      if (basedOnTemplate != null) {
        String basedOnTemplateId = basedOnTemplate.getValue();
        if (isSubmittable(basedOnTemplateId)) {
          currentUserPermissions.setCanSubmit(true);
        }
      }
    }
    currentUserPermissions.setCanCopy(true);
    currentUserPermissions.setCanMakePublic(resource.isPublic() == null || !resource.isPublic());
    currentUserPermissions.setCanMakeNotPublic(resource.isPublic() != null && resource.isPublic());
  }

  private boolean isSubmittable(String basedOnTemplateId) {
    return cedarConfig.getSubmissionConfig().getSubmittableTemplateIds() != null &&
        cedarConfig.getSubmissionConfig().getSubmittableTemplateIds().contains(basedOnTemplateId);
  }

}
