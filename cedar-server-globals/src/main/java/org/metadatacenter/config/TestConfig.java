package org.metadatacenter.config;

public class TestConfig extends ServerConfig {

  private int port;

  private int timeout;

  private TemplateTemplateTestConfig template;

  private TemplateElementTestConfig element;

  private TemplateFieldTestConfig field;

  private TemplateInstanceTestConfig instance;

  public int getPort() {
    return port;
  }

  public int getTimeout() {
    return timeout;
  }

  public TemplateTemplateTestConfig getTemplate() {
    return template;
  }

  public TemplateElementTestConfig getElement() {
    return element;
  }

  public TemplateFieldTestConfig getField() {
    return field;
  }

  public TemplateInstanceTestConfig getInstance() {
    return instance;
  }
}
