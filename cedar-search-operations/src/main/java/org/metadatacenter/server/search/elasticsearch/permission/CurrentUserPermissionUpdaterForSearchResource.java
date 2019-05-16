package org.metadatacenter.server.search.elasticsearch.permission;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.outcome.OutcomeWithReason;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.search.IndexedDocumentDocument;
import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;
import org.metadatacenter.server.security.model.user.CedarUser;

public class CurrentUserPermissionUpdaterForSearchResource extends AbstractCurrentUserPermissionUpdaterForSearch {

  private CurrentUserPermissionUpdaterForSearchResource(IndexedDocumentDocument indexedDocument, CedarUser cedarUser,
                                                        CedarConfig cedarConfig) {
    super(indexedDocument, cedarUser, cedarConfig);
  }

  public static CurrentUserPermissionUpdater get(IndexedDocumentDocument indexedDocument, CedarUser cedarUser,
                                                 CedarConfig cedarConfig) {
    return new CurrentUserPermissionUpdaterForSearchResource(indexedDocument, cedarUser, cedarConfig);
  }

  @Override
  public void update(CurrentUserPermissions currentUserPermissions) {
    if (userCanWrite()) {
      currentUserPermissions.setCanWrite(true);
      currentUserPermissions.setCanDelete(true);
      currentUserPermissions.setCanRead(true);
      currentUserPermissions.setCanShare(true);
    } else if (userCanRead()) {
      currentUserPermissions.setCanRead(true);
    }

    if (userCanChangeOwnerOfFolder()) {
      currentUserPermissions.setCanChangeOwner(true);
    }

    currentUserPermissions.setCanCopy(true);

    if (indexedDocument.getInfo().getType() == CedarResourceType.TEMPLATE) {
      currentUserPermissions.setCanPopulate(true);
    }

    OutcomeWithReason versioningOutcome = userCanPerformVersioning();
    if (versioningOutcome.isNegative()) {
      currentUserPermissions.setCreateDraftErrorKey(versioningOutcome.getReason());
      currentUserPermissions.setPublishErrorKey(versioningOutcome.getReason());
    } else {
      OutcomeWithReason publishOutcome = resourceCanBePublished();
      if (publishOutcome.isPositive()) {
        currentUserPermissions.setCanPublish(true);
      } else {
        currentUserPermissions.setPublishErrorKey(publishOutcome.getReason());
      }
      OutcomeWithReason createDraftOutcome = resourceCanBeDrafted();
      if (createDraftOutcome.isPositive()) {
        currentUserPermissions.setCanCreateDraft(true);
      } else {
        currentUserPermissions.setCreateDraftErrorKey(createDraftOutcome.getReason());
      }
    }

    if (indexedDocument.getInfo().getType() == CedarResourceType.INSTANCE) {
      if (isSubmittable()) {
        currentUserPermissions.setCanSubmit(true);
      }
    }

    currentUserPermissions.setCanMakeOpen(userCanWrite() && !indexedDocument.getInfo().getIsOpen());
    currentUserPermissions.setCanMakeNotOpen(userCanWrite() && indexedDocument.getInfo().getIsOpen());
  }

  private boolean isSubmittable() {
    ResourceUri basedOnTemplate = indexedDocument.getInfo().getIsBasedOn();
    if (basedOnTemplate != null) {
      String basedOnTemplateId = basedOnTemplate.getValue();
      return cedarConfig.getSubmissionConfig().getSubmittableTemplateIds() != null &&
          cedarConfig.getSubmissionConfig().getSubmittableTemplateIds().contains(basedOnTemplateId);
    }
    return false;
  }
}
