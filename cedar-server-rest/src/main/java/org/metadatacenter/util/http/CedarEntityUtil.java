package org.metadatacenter.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.rest.exception.CedarAssertionException;

import java.io.IOException;

public abstract class CedarEntityUtil {

  private CedarEntityUtil() {
  }

  public static String toString(HttpEntity entity) throws CedarAssertionException {
    String es;
    try {
      es = EntityUtils.toString(entity);
    } catch (IOException e) {
      throw new CedarAssertionException(e);
    }
    return es;
  }
}
