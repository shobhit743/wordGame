package com.shobhit.wordgame.dagger

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.shobhit.wordgame.App
import com.shobhit.wordgame.view.MainActivity
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Created by shobhit on 26/08/18.
 */
object AppInjector {

  fun init(app: App) {
    var daggerAppComponent = DaggerAppComponent.builder().application(app).build()
    daggerAppComponent.inject(app)

    app
        .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
          override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            handleActivity(activity)
          }

          override fun onActivityStarted(activity: Activity) {

          }

          override fun onActivityResumed(activity: Activity) {

          }

          override fun onActivityPaused(activity: Activity) {

          }

          override fun onActivityStopped(activity: Activity) {

          }

          override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

          }

          override fun onActivityDestroyed(activity: Activity) {

          }
        })
  }

  private fun handleActivity(activity: Activity) {
    if (activity is HasSupportFragmentInjector) {
      AndroidInjection.inject(activity)
    }
    if (activity is MainActivity) {
      activity.supportFragmentManager
          .registerFragmentLifecycleCallbacks(
              object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {

                  AndroidSupportInjection.inject(f)
                }
              }, true
          )
    }
  }
}
