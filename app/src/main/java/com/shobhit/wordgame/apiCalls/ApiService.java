package com.shobhit.wordgame.apiCalls;

import com.shobhit.wordgame.model.Data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by shobhit on 25/08/18.
 */

public interface ApiService {

  @Headers("Content-Type: application/json")
  @GET("?action=query&format=json&redirects=return&prop=extracts&exsentences=10")
  Observable<Data> getGameText(@Query("titles") String title);



}
