package org.metadatacenter.server.neo4j;

import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.result.BackendCallErrorType;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupUsersRequestValidator {

  private final CedarGroupUsersRequest request;
  private Neo4JUserSession neo4JUserSession;
  private BackendCallResult callResult;
  private CedarGroupUsers users;
  private String groupURL;

  private CedarFSGroup group;

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
    group = neo4JUserSession.findGroupById(groupURL);
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
