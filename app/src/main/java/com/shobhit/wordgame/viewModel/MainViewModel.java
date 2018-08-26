package com.shobhit.wordgame.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.shobhit.wordgame.apiCalls.ApiService;
import com.shobhit.wordgame.model.IdName;
import com.shobhit.wordgame.repository.MainRespository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by shobhit on 25/08/18.
 */

public class MainViewModel extends AndroidViewModel{


  public  int gameScore = 0;

  private List<IdName> mList = new ArrayList<>();

  public List<IdName> getmList() {
    return mList;
  }

  public void setmList(List<IdName> mList) {
    this.mList = mList;
  }

  private HashMap<Integer,String> sentenceMap = new HashMap<>();

  public HashMap<Integer, String> getSentenceMap() {
    return sentenceMap;
  }

  public void setSentenceMap(HashMap<Integer, String> sentenceMap) {
    this.sentenceMap = sentenceMap;
  }

  private HashMap<Integer, String[]> wordsMap = new HashMap<>();

  private HashMap<Integer,String> selectedWordsListMap = new HashMap<>();

  public HashMap<Integer, String> getSelectedWordsListMap() {
    return selectedWordsListMap;
  }

  public void setSelectedWordsListMap(HashMap<Integer, String> selectedWordsListMap) {
    this.selectedWordsListMap = selectedWordsListMap;
  }

  public HashMap<Integer, String[]> getWordsMap() {
    return wordsMap;
  }

  public void setWordsMap(HashMap<Integer, String[]> wordsMap) {
    this.wordsMap = wordsMap;
  }

  private MutableLiveData<String> gameMainText = new MutableLiveData<>();
  @Inject
  public MainViewModel(Application application,ApiService apiService){
    super(application);
    gameMainText = new MutableLiveData<>();
    Log.d("tag",apiService.toString());
    gameMainText= new MainRespository(apiService).getQuestion("laptop");
  }
  public MutableLiveData<String> getGameMainText() {
    return gameMainText;
  }
  public void setGameMainText(MutableLiveData<String> gameMainText) {
    this.gameMainText = gameMainText;
  }
}
