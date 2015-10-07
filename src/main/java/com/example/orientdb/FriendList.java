package com.example.orientdb;

import java.util.List;

public class FriendList {
  private List<Group> groups;
  private String uuid;

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
