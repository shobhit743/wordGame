package com.shobhit.wordgame.dagger

import android.app.Application
import com.shobhit.wordgame.App
import com.shobhit.wordgame.viewModel.MainViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by shobhit on 26/08/18.
 */
@Singleton
@Component(
    modules = [
      AndroidInjectionModule::class,
      AppModule::class,MainActivityModule::class]
)
 interface AppComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }
  fun inject(app: App)
  fun inject(app:MainViewModel)

}
