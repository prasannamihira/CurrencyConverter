package com.paypay.currencyapp.di

import android.preference.PreferenceManager
import com.paypay.currencyapp.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.paypay.currencyapp.api.ApiService
import com.paypay.currencyapp.app.App
import com.paypay.currencyapp.util.Config.Companion.TIME_OUT
import com.paypay.currencyapp.util.Config.Companion.TIME_OUT_RW
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    @ForApplication
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideSharedPreference() = PreferenceManager.getDefaultSharedPreferences(app)!!

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT_RW, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT_RW, TimeUnit.SECONDS)
                    .addNetworkInterceptor(StethoInterceptor())
                    .addInterceptor(interceptor).build()
            )
            .build()
            .create(ApiService::class.java)
    }
}