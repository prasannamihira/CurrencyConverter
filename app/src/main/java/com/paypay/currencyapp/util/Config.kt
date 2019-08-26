package com.paypay.currencyapp.util

interface Config {

    companion object {
        const val TIME_OUT = 300L
        const val TIME_OUT_RW = 300L

        const val CONTENT_TYPE_JSON = "Content-Type:application/json"

        const val AUTHORIZATION = "Authorization"
    }
}