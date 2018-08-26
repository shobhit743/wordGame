package com.shobhit.wordgame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shobhit on 26/08/18.
 */

public class Query {
  public HashMap<String, PageModel> getPages() {
    return pages;
  }

  public void setPages(HashMap<String, PageModel> pages) {
    this.pages = pages;
  }

  private HashMap<String,PageModel> pages;

}
