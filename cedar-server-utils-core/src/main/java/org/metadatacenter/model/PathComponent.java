package org.metadatacenter.model;

public class PathComponent {

  private String name;
  private boolean userHome;

  public PathComponent() {
  }

  public PathComponent(String name, boolean userHome) {
    this.name = name;
    this.userHome = userHome;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isUserHome() {
    return userHome;
  }

  public void setUserHome(boolean userHome) {
    this.userHome = userHome;
  }
}
