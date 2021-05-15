package org.metadatacenter.util;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.extract.FolderServerArtifactExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.model.folderserver.report.FolderServerArtifactReport;

import java.util.List;

public class TrustedByUtil {

  public static void decorateWithTrustedby(FolderServerResourceExtract resourceExtract,
                                           List<FolderServerResourceExtract> pathInfo) {
    decorateWithTrustedby(resourceExtract, extractParentFolderIdFromPathInfo(pathInfo));
  }

  public static void decorateWithTrustedby(FolderServerArtifactReport artifactReport) {
    String parentFolderId = extractParentFolderIdFromPathInfo(artifactReport.getPathInfo());
    artifactReport.setTrustedBy(generateTrustedBy(parentFolderId, artifactReport.getType()));
  }

  public static void decorateWithTrustedby(FolderServerResourceExtract resourceExtract, String parentFolderId) {
    if (!resourceExtract.getType().equals(CedarResourceType.FOLDER)) { // cast exception when using folders
      ((FolderServerArtifactExtract) resourceExtract).
          setTrustedBy(generateTrustedBy(parentFolderId, resourceExtract.getType()));
    }
  }

  private static String generateTrustedBy(String parentFolderId, CedarResourceType resourceType) {
    if (resourceType.equals(CedarResourceType.TEMPLATE) ||
        resourceType.equals(CedarResourceType.ELEMENT) ||
        resourceType.equals(CedarResourceType.FIELD) ||
        resourceType.equals(CedarResourceType.INSTANCE)) {
      if (parentFolderId.equals("https://repo.metadatacenter.orgx/folders/132596f8-ef92-4cc0-8c0d-30e61a658631") ||
          parentFolderId.equals("https://repo.metadatacenter.orgx/folders/68ae7a84-c6a0-43c0-9a2f-4cc23a148a90")) {
        return "caDSR";
      }
    }
    return null;
  }

  private static String extractParentFolderIdFromPathInfo(List<FolderServerResourceExtract> pathInfo) {
    if (pathInfo.size() == 0) {
      return null;
    }
    if (pathInfo.get(pathInfo.size() - 1).getType().equals(CedarResourceType.FOLDER)) {
      return pathInfo.get(pathInfo.size() - 1).getId();
    } else {
      return pathInfo.get(pathInfo.size() - 2).getId();
    }
  }


}



