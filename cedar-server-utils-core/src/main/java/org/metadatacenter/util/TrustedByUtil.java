package org.metadatacenter.util;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.extract.FolderServerArtifactExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.model.folderserver.report.FolderServerArtifactReport;

import java.util.List;

public class TrustedByUtil {

  public static void decorateWithTrustedby(FolderServerResourceExtract resourceExtract,
                                           List<FolderServerResourceExtract> pathInfo) {
    if (resourceExtract.getType().equals(CedarResourceType.TEMPLATE) ||
        resourceExtract.getType().equals(CedarResourceType.ELEMENT) ||
        resourceExtract.getType().equals(CedarResourceType.FIELD) ||
        resourceExtract.getType().equals(CedarResourceType.INSTANCE)) {
      String id = pathInfo.get(pathInfo.size() - 1).getId();
      if (id.equals("https://repo.metadatacenter.orgx/folders/132596f8-ef92-4cc0-8c0d-30e61a658631")) {
        ((FolderServerArtifactExtract)resourceExtract).setTrustedBy("caDSR");
      }
    }
  }

  public static void decorateWithTrustedby(FolderServerArtifactReport artifactReport) {
    if (artifactReport.getType().equals(CedarResourceType.TEMPLATE) ||
        artifactReport.getType().equals(CedarResourceType.ELEMENT) ||
        artifactReport.getType().equals(CedarResourceType.FIELD) ||
        artifactReport.getType().equals(CedarResourceType.INSTANCE)) {
      String id = artifactReport.getPathInfo().get(artifactReport.getPathInfo().size() - 2).getId();
      if (id.equals("https://repo.metadatacenter.orgx/folders/132596f8-ef92-4cc0-8c0d-30e61a658631")) {
        artifactReport.setTrustedBy("caDSR");
      }
    }
  }


}



