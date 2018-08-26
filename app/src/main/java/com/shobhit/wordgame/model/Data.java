package com.shobhit.wordgame.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shobhit on 26/08/18.
 */

public class Data {

  @SerializedName("query")
  private Query query;

  public Query getQuery() {
    return query;
  }

  public void setQuery(Query query) {
    this.query = query;
  }
}
