package com.paypay.currencyapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.paypay.currencyapp.R
import com.paypay.currencyapp.base.BaseActivity
import com.paypay.currencyapp.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private var mBinding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        goToNext()
    }

    private fun goToNext() {
        Handler().postDelayed({
            startCurrencyConverter()
        }, 1000)
    }
}
