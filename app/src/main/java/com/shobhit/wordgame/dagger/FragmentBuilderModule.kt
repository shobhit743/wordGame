package com.shobhit.wordgame.dagger

import com.shobhit.wordgame.view.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by shobhit on 26/08/18.
 */

@Module
abstract class FragmentBuilderModule {
  @ContributesAndroidInjector
  abstract fun contributeMainFragment(): MainFragment
}