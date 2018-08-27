package com.shobhit.wordgame.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.shobhit.wordgame.apiCalls.ApiService;
import com.shobhit.wordgame.model.IdName;
import com.shobhit.wordgame.repository.MainRespository;
import com.shobhit.wordgame.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by shobhit on 25/08/18.
 */

public class MainViewModel extends AndroidViewModel {

  private String[] titleArray = {"laptop", "television","movie", "computer",
      "facebook","telephone"};

  public int gameScore = 0;

  private List<IdName> mList = new ArrayList<>();

  public List<IdName> getmList() {
    return mList;
  }

  public void setmList(List<IdName> mList) {
    this.mList = mList;
  }

  private MutableLiveData<String> gameMainText = new MutableLiveData<>();

  private MutableLiveData<String> gameTitle = new MutableLiveData<>();

  public MutableLiveData<String> getGameTitle() {
    return gameTitle;
  }

  public void setGameTitle(MutableLiveData<String> gameTitle) {
    this.gameTitle = gameTitle;
  }

  @Inject
  public MainViewModel(Application application, ApiService apiService) {
    super(application);
    gameMainText = new MutableLiveData<>();
    String title = titleArray[CommonUtils.getRandomDoubleBetweenRange(0, titleArray.length - 1)];
    gameTitle.setValue(title);
    gameMainText = new MainRespository(apiService).getQuestion(title);
  }

  public MutableLiveData<String> getGameMainText() {
    return gameMainText;
  }

  public void setGameMainText(MutableLiveData<String> gameMainText) {
    this.gameMainText = gameMainText;
  }
}
