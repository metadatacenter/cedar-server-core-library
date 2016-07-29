package org.metadatacenter.config;

public class CedarConfigTest {

  public static void main(String[] args) {
    CedarConfig config = CedarConfig.getInstance();
    System.out.println(config.getBlueprintUIPreferences().getPopulateATemplate().getOpened());
  }

}
