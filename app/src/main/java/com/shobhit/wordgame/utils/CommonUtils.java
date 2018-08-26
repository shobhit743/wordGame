package com.shobhit.wordgame.utils;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shobhit on 26/08/18.
 */

public class CommonUtils {

  public static List<String> getListOfSentences(BreakIterator bi, String source) {
    List<String> mList = new ArrayList<>();
    int counter = 0;
    bi.setText(source);
    int lastIndex = bi.first();
    while (lastIndex != BreakIterator.DONE) {
      int firstIndex = lastIndex;
      lastIndex = bi.next();

      if (lastIndex != BreakIterator.DONE) {
        mList.add(source.substring(firstIndex, lastIndex));
        counter++;
      }
    }
    return mList;
  }

  public static String[] getListOfWords(String sentence){
    String[] words = sentence.split("\\s+");
    for (int i = 0; i < words.length; i++) {
      words[i] = words[i].replaceAll("[^\\w]", "");
    }
    return words;
  }


  public static int getRandomDoubleBetweenRange(int min, int max){
    return (int) (Math.random()*((max-min)+1))+min;
  }

}
