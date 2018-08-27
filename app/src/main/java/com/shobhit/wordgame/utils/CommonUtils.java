package com.shobhit.wordgame.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.shobhit.wordgame.R;
import com.shobhit.wordgame.model.IdName;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shobhit on 26/08/18.
 */

public class CommonUtils {

  public static List<String> getListOfSentences(BreakIterator bi, String source) {
    List<String> mList = new ArrayList<>();
    bi.setText(source);
    int lastIndex = bi.first();
    while (lastIndex != BreakIterator.DONE) {
      int firstIndex = lastIndex;
      lastIndex = bi.next();
      if (lastIndex != BreakIterator.DONE) {
        mList.add(source.substring(firstIndex, lastIndex));
      }
    }
    return mList;
  }

  public static String[] getListOfWords(String sentence) {
    String[] words = sentence.split("\\s+");
    for (int i = 0; i < words.length; i++) {
      words[i] = words[i].replaceAll("[^\\w]", "");
    }
    return words;
  }

  public static void revealShow(View dialogView, boolean b, final Dialog dialog, FloatingActionButton fab) {

    final View view = dialogView.findViewById(R.id.dialog);

    int w = view.getWidth();
    int h = view.getHeight();

    int endRadius = (int) Math.hypot(w, h);

    int cx = (int) (fab.getX() + (fab.getWidth() / 2));
    int cy = (int) (fab.getY()) + fab.getHeight() + 56;


    if (b) {
      Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);

      view.setVisibility(View.VISIBLE);
      revealAnimator.setDuration(500);
      revealAnimator.start();

    } else {

      Animator anim =
          ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

      anim.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);
          dialog.dismiss();
          view.setVisibility(View.INVISIBLE);

        }
      });
      anim.setDuration(500);
      anim.start();
    }

  }

  public static List<IdName> randomize(List<IdName> mList) {
    Random r = new Random();
    for (int i = mList.size() - 1; i > 0; i--) {
      int j = r.nextInt(i);
      IdName temp = mList.get(i);
      mList.remove(i);
      mList.add(i, mList.get(j));
      mList.remove(j);
      mList.add(j, temp);
    }
    return mList;
  }


  public static int getRandomDoubleBetweenRange(int min, int max) {
    return (int) (Math.random() * ((max - min) + 1)) + min;
  }

}
