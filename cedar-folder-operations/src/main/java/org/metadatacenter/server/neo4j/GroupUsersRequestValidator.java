package org.metadatacenter.server.neo4j;

import org.metadatacenter.model.folderserver.CedarFSGroup;
import org.metadatacenter.server.result.BackendCallErrorType;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.List;

public class GroupUsersRequestValidator {

  private final CedarGroupUsersRequest request;
  private final Neo4JUserSession neo4JUserSession;
  private final BackendCallResult callResult;
  private final CedarGroupUsers users;
  private final String groupURL;

  public GroupUsersRequestValidator(Neo4JUserSession neo4JUserSession, String groupURL, CedarGroupUsersRequest
      request) {
    this.neo4JUserSession = neo4JUserSession;
    this.callResult = new BackendCallResult();
    this.request = request;
    this.groupURL = groupURL;
    this.users = new CedarGroupUsers();

    validateNodeExistence();

    if (callResult.isOk()) {
      validateAndSetUsers();
    }
  }

  private void validateNodeExistence() {
    CedarFSGroup group = neo4JUserSession.findGroupById(groupURL);
    if (group == null) {
      callResult.addError(BackendCallErrorType.NOT_FOUND)
          .subType("groupNotFound")
          .message("Group not found by id")
          .param("groupId", groupURL);
    }
  }

  private void validateAndSetUsers() {
    List<CedarGroupUserRequest> requestUsers = request.getUsers();
    for (CedarGroupUserRequest u : requestUsers) {
      NodePermissionUser groupUser = u.getUser();
      if (groupUser == null) {
        callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
            .subType("userNodeMissing")
            .message("The user node is missing from the request");
      } else {
        users.addUser(new CedarGroupUser(
                new CedarUserExtract(groupUser.getId(), null, null, null), u.isAdministrator(), u.isMember())
        );
      }
    }
  }

  public BackendCallResult getCallResult() {
    return callResult;
  }

  public CedarGroupUsers getUsers() {
    return users;
  }
}
