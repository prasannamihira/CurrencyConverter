package com.paypay.currencyapp.ui.currency

import android.content.SharedPreferences
import com.paypay.currencyapp.BuildConfig
import com.paypay.currencyapp.api.ApiService
import io.reactivex.Flowable
import org.json.JSONObject
import javax.inject.Inject

class CurrencyConverterVM @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val apiService: ApiService
) {

    private var errorMessage: String = ""
    var querySuccess: Boolean = false
    var currencyData: Map<String, Double>? = null
    var sourceCountryCode: String? = null
    private var accessToken: String = BuildConfig.ACCESS_TOKEN

    /**
     * Fetch all live currency data
     */
    fun fetchCurrencyData(): Flowable<Unit> =
        apiService.fetchCurrencyData(accessToken, "1")
            .map {
                if (it.isSuccessful) {
                    currencyData = it.body()!!.quotes
                    sourceCountryCode = it.body()!!.source

                    if (currencyData != null) {
                        querySuccess = true
                    }
                } else {

                    // update profile error
                    errorMessage = try {
                        val jObjError = JSONObject(it.errorBody()?.string())
                        var errorMessages = jObjError.getJSONArray("errors")
                        errorMessages.getJSONObject(0).getString("message")
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                }
            }

    /**
     * Fetch specific currency data by source value
     *
     * @param currenciesQuery
     * @param sourceString
     */
    fun fetchCurrencyDataBySourceValue(
        currenciesQuery: String,
        sourceString: String
    ): Flowable<Unit> =
        apiService.fetchCurrencyDataBySource(accessToken, currenciesQuery, sourceString, "1")
            .map {
                if (it.isSuccessful) {
                    currencyData = it.body()!!.quotes

                    if (currencyData != null) {
                        querySuccess = true
                    }
                } else {

                    // update profile error
                    errorMessage = try {
                        val jObjError = JSONObject(it.errorBody()?.string())
                        var errorMessages = jObjError.getJSONArray("errors")
                        errorMessages.getJSONObject(0).getString("message")
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                }
            }
}