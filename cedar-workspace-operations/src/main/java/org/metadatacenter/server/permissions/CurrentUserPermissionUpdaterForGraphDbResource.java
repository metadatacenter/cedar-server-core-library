package org.metadatacenter.server.permissions;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithOpenFlag;
import org.metadatacenter.outcome.OutcomeWithReason;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.VersionServiceSession;
import org.metadatacenter.server.security.model.InstanceArtifactWithIsBasedOn;
import org.metadatacenter.server.security.model.auth.CurrentUserResourcePermissions;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;

public class CurrentUserPermissionUpdaterForGraphDbResource extends CurrentUserPermissionUpdater {

  private final PermissionServiceSession permissionSession;
  private final VersionServiceSession versionSession;
  private final CedarConfig cedarConfig;
  private final ResourceWithCurrentUserPermissions resource;

  private CurrentUserPermissionUpdaterForGraphDbResource(PermissionServiceSession permissionSession,
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
    return new CurrentUserPermissionUpdaterForGraphDbResource(permissionSession, versionSession, cedarConfig,
        resource);
  }

  @Override
  public void update(CurrentUserResourcePermissions currentUserResourcePermissions) {
    String id = resource.getId();
    if (permissionSession.userHasWriteAccessToNode(id)) {
      currentUserResourcePermissions.setCanWrite(true);
      currentUserResourcePermissions.setCanDelete(true);
      currentUserResourcePermissions.setCanRead(true);
      currentUserResourcePermissions.setCanShare(true);
    } else if (permissionSession.userHasReadAccessToNode(id)) {
      currentUserResourcePermissions.setCanRead(true);
    }
    if (permissionSession.userCanChangeOwnerOfNode(id)) {
      currentUserResourcePermissions.setCanChangeOwner(true);
    }
    OutcomeWithReason versioningOutcome = versionSession.userCanPerformVersioning(resource);
    if (versioningOutcome.isNegative()) {
      currentUserResourcePermissions.setCreateDraftErrorKey(versioningOutcome.getReason());
      currentUserResourcePermissions.setPublishErrorKey(versioningOutcome.getReason());
    } else {
      OutcomeWithReason publishOutcome = versionSession.resourceCanBePublished(resource);
      if (publishOutcome.isPositive()) {
        currentUserResourcePermissions.setCanPublish(true);
      } else {
        currentUserResourcePermissions.setPublishErrorKey(publishOutcome.getReason());
      }
      OutcomeWithReason createDraftOutcome = versionSession.resourceCanBeDrafted(resource);
      if (createDraftOutcome.isPositive()) {
        currentUserResourcePermissions.setCanCreateDraft(true);
      } else {
        currentUserResourcePermissions.setCreateDraftErrorKey(createDraftOutcome.getReason());
      }
    }
    if (resource.getType() == CedarResourceType.TEMPLATE) {
      currentUserResourcePermissions.setCanPopulate(true);
    }
    if (resource.getType() == CedarResourceType.INSTANCE) {
      InstanceArtifactWithIsBasedOn instance = (InstanceArtifactWithIsBasedOn) resource;
      ResourceUri basedOnTemplate = instance.getIsBasedOn();
      if (basedOnTemplate != null) {
        String basedOnTemplateId = basedOnTemplate.getValue();
        if (isSubmittable(basedOnTemplateId)) {
          currentUserResourcePermissions.setCanSubmit(true);
        }
      }
    }
    currentUserResourcePermissions.setCanCopy(true);
    if (resource instanceof ResourceWithOpenFlag) {
      ResourceWithOpenFlag res = (ResourceWithOpenFlag) resource;
      currentUserResourcePermissions.setCanMakeOpen(res.isOpen() == null || !res.isOpen());
      currentUserResourcePermissions.setCanMakeNotOpen(res.isOpen() != null && res.isOpen());
    } else {
      currentUserResourcePermissions.setCanMakeOpen(false);
      currentUserResourcePermissions.setCanMakeNotOpen(false);
    }
  }

  private boolean isSubmittable(String basedOnTemplateId) {
    return cedarConfig.getSubmissionConfig().getSubmittableTemplateIds() != null &&
        cedarConfig.getSubmissionConfig().getSubmittableTemplateIds().contains(basedOnTemplateId);
  }

}
