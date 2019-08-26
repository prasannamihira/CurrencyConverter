package com.paypay.currencyapp.api

import com.paypay.currencyapp.data.api.response.CurrencyData
import com.paypay.currencyapp.util.Config
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    /**
     * query all live currency data
     *
     * @param accessKey
     * @param format
     */
    @Headers(Config.CONTENT_TYPE_JSON)
    @GET("live")
    fun fetchCurrencyData(
        @Query("access_key") accessKey: String
        , @Query("format") format: String
    ): Flowable<Response<CurrencyData>>


    /**
     * query specific currency data using source value
     *
     * @param accessKey
     * @param currencies
     * @param source
     * @param format
     */
    @Headers(Config.CONTENT_TYPE_JSON)
    @GET("live")
    fun fetchCurrencyDataBySource(
        @Query("access_key") accessKey: String
        , @Query("currencies") currencies: String
        , @Query("source") source: String
        , @Query("format") format: String
    ): Flowable<Response<CurrencyData>>

}