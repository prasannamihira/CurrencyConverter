package com.paypay.currencyapp.app

import android.app.Application
import com.paypay.currencyapp.data.model.CountryCode
import com.paypay.currencyapp.di.AppComponent
import com.paypay.currencyapp.di.AppModule
import com.paypay.currencyapp.di.DaggerAppComponent
import java.util.ArrayList

class App: Application() {

    companion object {
        lateinit var instance: App
        lateinit var appComponent: AppComponent

        // currency data
        var currencyData: Map<String, Double>? = null
        var sourceCountryCode: String? = null
        var countryCodeList: ArrayList<CountryCode>? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        appComponent.inject(this)
    }

    fun setCurrencyData(currencyData: Map<String, Double>?) {
        App.currencyData = currencyData
    }

    fun getCurrencyData(): Map<String, Double>? {
        return currencyData
    }

    fun setSourceCountryCode(sourceCountryCode: String?) {
        App.sourceCountryCode = sourceCountryCode
    }

    fun getSourceCountryCode(): String? {
        return sourceCountryCode
    }

    fun setCountryCodeList(countryCodeList: ArrayList<CountryCode>?) {
        App.countryCodeList = countryCodeList
    }

    fun getCountryCodeList(): ArrayList<CountryCode>? {
        return countryCodeList
    }

}