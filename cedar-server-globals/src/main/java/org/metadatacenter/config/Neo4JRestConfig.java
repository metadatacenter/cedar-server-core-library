package org.metadatacenter.config;

import org.apache.commons.codec.binary.Base64;

public class Neo4JRestConfig {

  private String transactionUrl;

  private String userName;

  private String userPassword;

  public String getTransactionUrl() {
    return transactionUrl;
  }

  public String getAuthString() {
    return "Basic " + new String(Base64.encodeBase64((userName + ":" + userPassword).getBytes()));
  }

  public String getUserName() {
    return userName;
  }

  public String getUserPassword() {
    return userPassword;
  }
}
