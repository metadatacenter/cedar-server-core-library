package org.metadatacenter.server.search.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.util.http.ProxyUtil;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FolderServerUtil {

  public static Map<String, String> getAccessibleNodeIds(String folderBase, CedarRequestContext context) throws
      CedarProcessingException {
    String url = folderBase + "/" + "accessible-node-ids";
    Map<String, String> accessibleNodeIds = null;
    try {
      HttpResponse proxyResponse = ProxyUtil.proxyGet(url, context);
      int statusCode = proxyResponse.getStatusLine().getStatusCode();

      HttpEntity entity = proxyResponse.getEntity();
      if (entity != null) {
        if (HttpStatus.SC_OK == statusCode) {
          String responseString = EntityUtils.toString(proxyResponse.getEntity());
          Map<String, Map<String, String>> accessibleNodeIdsObject =
              JsonMapper.MAPPER.readValue(responseString, new TypeReference<Map<String, HashMap<String, String>>>() {
              });
          accessibleNodeIds = accessibleNodeIdsObject.get("accessibleNodes");
        }
      }
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
    return accessibleNodeIds;
  }
}