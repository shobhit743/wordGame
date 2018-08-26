package com.shobhit.wordgame.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shobhit on 26/08/18.
 */

public class PageModel {

  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("extract")
  @Expose
  private String extract;


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getExtract() {
    return extract;
  }

  public void setExtract(String extract) {
    this.extract = extract;
  }
}
