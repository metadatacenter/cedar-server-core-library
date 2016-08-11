package org.metadatacenter.config;

public interface TestConfig extends ServerConfig {

  int getPort();

  int getTimeout();

  TemplateTestConfig getTemplate();

  TemplateElementTestConfig getElement();

  TemplateFieldTestConfig getField();

  TemplateInstanceTestConfig getInstance();

}
