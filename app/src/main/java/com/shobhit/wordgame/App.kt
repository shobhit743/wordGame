package com.shobhit.wordgame

import android.app.Activity
import android.app.Application
import com.shobhit.wordgame.dagger.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by shobhit on 26/08/18.
 */
class App :Application(), HasActivityInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()
    AppInjector.init(this)
  }

  override fun activityInjector() = dispatchingAndroidInjector
}
