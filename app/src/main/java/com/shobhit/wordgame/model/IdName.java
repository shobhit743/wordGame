package com.shobhit.wordgame.model;

/**
 * Created by shobhit on 26/08/18.
 */

public class IdName {
  private Integer id;
  private String name;
  private String correctName;
  private boolean valueInserted;

  public boolean isValueInserted() {
    return valueInserted;
  }

  public void setValueInserted(boolean valueInserted) {
    this.valueInserted = valueInserted;
  }

  public String getCorrectName() {
    return correctName;
  }

  public void setCorrectName(String correctName) {
    this.correctName = correctName;
  }

  private boolean isBlank = true;

  public boolean isBlank() {
    return isBlank;
  }

  public void setBlank(boolean blank) {
    isBlank = blank;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    IdName idName = (IdName) obj;
    if(idName.getId()== this.getId()){
      return true;
    } else {
      return false;
    }
  }
}
