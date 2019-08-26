package com.paypay.currencyapp.ui.currency

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.paypay.currencyapp.R
import com.paypay.currencyapp.app.App
import com.paypay.currencyapp.base.BaseActivity
import com.paypay.currencyapp.data.model.CountryCode
import com.paypay.currencyapp.databinding.ActivityCurrencyConverterBinding
import com.paypay.currencyapp.ui.common.fragment.CountryCodeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CurrencyConverterActivity : BaseActivity(), View.OnClickListener {

    private var mBinding: ActivityCurrencyConverterBinding? = null

    // activity context
    private var context: Context = this

    // currency rates map
    var currencyData: Map<String, Double>? = null
    private var countryCode: String? = null
    private var countryCodeList = ArrayList<CountryCode>()

    @Inject
    lateinit var currencyConverterVM: CurrencyConverterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appComponent.inject(this)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_currency_converter)

        mBinding?.topBar?.tvTopBarTitle?.setText(R.string.title_currency_converter)

        mBinding?.llFlagSelect?.setOnClickListener(this)

        populateCurrencyData()

        mBinding?.etEditAmount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var amountValue = 0.0

                var amount = mBinding?.etEditAmount?.text.toString()
                if (amount.isNotEmpty()) {
                    amountValue = amount.toDouble()
                    if (amount != "1") {
                        amountValue = amount.toDouble()
                    } else {
                        amountValue = 1.0
                    }

                } else {
                    amountValue = 0.0
                }

                currencyData = App.instance.getCurrencyData()!!

                countryCodeList.clear()

                currencyData!!.forEach { (code, rate) ->

                    var currencyCode = code.substring(3, 6)
                    var countryCode = code.substring(3, 5)
                    var rateValue = rate * amountValue
                    val number2digits: Double = String.format("%.2f", rateValue).toDouble()
                    countryCodeList!!.add(
                        CountryCode(
                            currencyCode,
                            number2digits.toString(),
                            countryCode.toLowerCase()
                        )
                    )
                }
                // Currency rate adapter
                var adapter = CurrencyRateListAdapter(countryCodeList, context)
                mBinding?.etEditAmount?.postDelayed(Runnable {
                    mBinding?.rvCountryRatesList?.adapter = adapter
                    // notify adapter when data changed
                    adapter.notifyDataSetChanged()
                }, 100)
            }

        })
    }

    override fun onClick(view: View?) {
        when (view) {

            mBinding?.llFlagSelect -> {
                popupCountryOfMobileNo()
            }
        }
    }

    private fun popupCountryOfMobileNo() {
        val fm = supportFragmentManager

        // Create and show the dialog.
        val newFragment = CountryCodeFragment(context)
        newFragment.parentActivity = "register"
        newFragment.show(fm, "dialog")
    }

    fun onUserSelectValue(selectedValue: CountryCode) {

        Log.e("ISO Code", "" + selectedValue.isoCode)

        val flagId =
            resources.getDrawable(
                resources.getIdentifier(
                    selectedValue.isoCode.toLowerCase(),
                    "drawable",
                    packageName
                )
            )

        mBinding?.ivFlagSelect?.setImageDrawable(flagId)
    }

    /**
     * Fetch all live currency data
     */
    private fun fetchCurrencyDataByUSD() {

        subscription.add(
            currencyConverterVM.fetchCurrencyData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress(mBinding!!.progress, true) }
                .doOnTerminate { showProgress(mBinding!!.progress, false) }
                .subscribe(
                    {
                        if (it != null && currencyConverterVM.querySuccess) {

                            var data = currencyConverterVM.currencyData
                            var sourceCountryCode = currencyConverterVM.sourceCountryCode
                            App.instance.setCurrencyData(data)
                            App.instance.setSourceCountryCode(sourceCountryCode)

                            Timber.d("Currency data fetch success")

                            populateCurrencyData()
                        } else {
                            showAlertOK("Currency data fetch failed")
                        }
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    /**
     * Fetch specific currency data by source value
     */
    private fun fetchCurrencyDataBySourceValue() {

        var sourceValue = "JPY"
        var queryValue = "EUR,GBP,CAD,PLN"

        subscription.add(
            currencyConverterVM.fetchCurrencyDataBySourceValue(queryValue, sourceValue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress(mBinding!!.progress, true) }
                .doOnTerminate { showProgress(mBinding!!.progress, false) }
                .subscribe(
                    {
                        if (it != null && currencyConverterVM.querySuccess) {

                            var data = currencyConverterVM.currencyData
                            App.instance.setCurrencyData(data)

                            Timber.d("Currency data fetch success")


                        } else {
                            showAlertOK("Currency data fetch failed")
                        }
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    private fun populateCurrencyData() {
        if (App.instance.getCurrencyData() != null) {
            currencyData = App.instance.getCurrencyData()!!

            countryCodeList.clear()

            currencyData!!.forEach { (code, rate) ->

                var currencyCode = code.substring(3, 6)
                var countryCode = code.substring(3, 5)
                val number2digits: Double = String.format("%.2f", rate).toDouble()
                countryCodeList!!.add(
                    CountryCode(
                        currencyCode,
                        number2digits.toString(),
                        countryCode.toLowerCase()
                    )
                )
            }

            App.instance.setCountryCodeList(countryCodeList)

            if (!currencyData.isNullOrEmpty()) {

                countryCode = App.instance.getSourceCountryCode()

                setupImageFlag(countryCode)

                mBinding?.etEditAmount?.setText("1")
                setupCountryRatesData(countryCodeList)
            }
        } else {
            fetchCurrencyDataByUSD()
        }
    }

    /**
     * setup flag image by country code
     *
     * @param countryCode
     */
    private fun setupImageFlag(countryCode: String?) {

        if (countryCode != null) {
            var isoCode: String = countryCode.substring(0, countryCode.length - 1)

            val flagId = resources.getDrawable(
                resources.getIdentifier(
                    isoCode.toLowerCase(),
                    "drawable",
                    packageName
                )
            )
            mBinding?.ivFlagSelect?.setImageDrawable(flagId)
        }
    }

    private fun setupCountryRatesData(countryCodeList: ArrayList<CountryCode>) {

        var adapter = CurrencyRateListAdapter(countryCodeList, this)
        mBinding?.rvCountryRatesList?.layoutManager = GridLayoutManager(this, 3)
        mBinding?.rvCountryRatesList?.adapter = adapter
    }
}