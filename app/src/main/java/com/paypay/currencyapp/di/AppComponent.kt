package com.paypay.currencyapp.di

import com.paypay.currencyapp.app.App
import com.paypay.currencyapp.ui.currency.CurrencyConverterActivity
import com.paypay.currencyapp.ui.offline.OfflineActivity
import com.paypay.currencyapp.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(app: App)
    fun inject(activity: OfflineActivity)
    fun inject(activity: CurrencyConverterActivity)
}