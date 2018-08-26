package com.shobhit.wordgame.dagger

import com.shobhit.wordgame.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by shobhit on 26/08/18.
 */
@Module
abstract class MainActivityModule {

  @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
  abstract fun contributeMainActivity(): MainActivity

}
