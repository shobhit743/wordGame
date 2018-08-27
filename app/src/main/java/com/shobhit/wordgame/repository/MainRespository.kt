package com.shobhit.wordgame.repository

import android.text.Html
import com.shobhit.wordgame.apiCalls.ApiService
import com.shobhit.wordgame.model.Data
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import android.arch.lifecycle.MutableLiveData
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by shobhit on 26/08/18.
 */
class MainRespository

constructor(val apiService: ApiService) {
  fun getQuestion(title: String) :MutableLiveData<String> {
    val liveDataStr = MutableLiveData<String>()
    apiService.getGameText(title).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(object : DisposableObserver<Data>() {

          override fun onNext(data: Data) {

            if (data?.query != null && data.query.pages != null) {
              for (pageModel in data.query.pages.values) {
                if (pageModel.extract != null) {
                   liveDataStr.value = (Html.fromHtml(pageModel.extract).toString())
                  break
                }
              }
            }
          }

          override fun onError(e: Throwable) {
            liveDataStr.value = null
          }

          override fun onComplete() {

          }
        })
    return liveDataStr
  }


}