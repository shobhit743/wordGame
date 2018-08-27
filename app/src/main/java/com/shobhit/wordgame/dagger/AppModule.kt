package com.shobhit.wordgame.dagger

import android.app.Application
import com.shobhit.wordgame.BuildConfig
import com.shobhit.wordgame.apiCalls.ApiService
import com.shobhit.wordgame.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import android.arch.lifecycle.ViewModelProvider



/**
 * Created by shobhit on 26/08/18.
 */

@Module(includes = [ViewModelModule::class])
class AppModule() {

  @Provides
  @Named(Constants.BASE_URL)
  internal fun provideBaseUrlString(): String {
    return Constants.BASE_URL
  }

  @Provides
  @Singleton
  internal fun provideOkHttpCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024 // 10 MiB
    return Cache(application.cacheDir, cacheSize.toLong())
  }


  @Provides
  @Singleton
  internal fun provideOkHttpClient(cache: Cache): OkHttpClient.Builder {
    val logging = HttpLoggingInterceptor()
    logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    val httpClient = OkHttpClient.Builder()
    httpClient.cache(cache)
    httpClient.addInterceptor(logging)
    httpClient.readTimeout(10, TimeUnit.SECONDS)
    httpClient.connectTimeout(10, TimeUnit.SECONDS)
    httpClient.writeTimeout(10, TimeUnit.SECONDS)
    httpClient.addInterceptor { chain ->
      val request = chain.request()
      chain.proceed(request)
    }
    return httpClient
  }

  @Provides
  @Singleton
  internal fun provideGsonConverter(): Converter.Factory {
    return GsonConverterFactory.create()
  }

  @Provides
  @Singleton
  internal fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
  }


  @Provides
  @Singleton
  internal fun provideRetrofit(rxJava2CallAdapterFactory: RxJava2CallAdapterFactory, converter: Converter.Factory, @Named(Constants.BASE_URL) baseUrl: String, okHttpClient: OkHttpClient.Builder): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converter)
        .addCallAdapterFactory(rxJava2CallAdapterFactory)
        .client(okHttpClient.build())
        .build()
  }

  @Provides
  @Singleton
  internal fun provideApiInterface(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
  }
}
