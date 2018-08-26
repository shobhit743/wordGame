package com.shobhit.wordgame.dagger

import android.arch.lifecycle.ViewModel
import com.shobhit.wordgame.viewModel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.shobhit.wordgame.viewModel.DaggerViewModelFactory
import android.arch.lifecycle.ViewModelProvider



/**
 * Created by shobhit on 26/08/18.
 */
@Module
abstract class ViewModelModule {

  @Binds
  internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(MainViewModel::class)
  abstract fun provideMainViewModel(userViewModel: MainViewModel): ViewModel
}