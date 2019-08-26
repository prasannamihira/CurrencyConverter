package com.paypay.currencyapp.data.api.response

data class CurrencyData(val success:Boolean, val timestamp: Long, val source:String, val quotes: Map<String, Double>)

