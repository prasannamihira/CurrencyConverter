package com.paypay.currencyapp.util

import android.content.Context
import android.net.ConnectivityManager
import timber.log.Timber

class AppUtil {

    companion object {

        /**
         * check the internet is available
         * @param context
         */
        fun hasInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val hasInternet = networkInfo != null && networkInfo.isConnectedOrConnecting
            Timber.d("internetChanged %s", hasInternet)
            return hasInternet
        }
    }
}