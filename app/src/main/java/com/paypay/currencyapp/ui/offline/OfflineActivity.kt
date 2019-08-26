package com.paypay.currencyapp.ui.offline

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paypay.currencyapp.R
import com.paypay.currencyapp.app.App
import com.paypay.currencyapp.base.BaseActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_offline.*
import java.util.concurrent.TimeUnit

class OfflineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        hideStatusBar()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline)
        App.appComponent.inject(this)

        webView.clearCache(false)
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.loadUrl("file:///android_asset/offline/offline_continues_img.html")
    }

    override fun needOfflineScreen(): Boolean {
        return false
    }

    /**
     * create internet change action
     */
    override fun internetChanged(hasInternet: Boolean) {
        super.internetChanged(hasInternet)
        if (hasInternet) {
            doAnimate(root)

            webView.loadUrl("file:///android_asset/offline/offline_gif.html")

            //close
            subscription.add(
                Observable.just(1)
                    .delay(1, TimeUnit.SECONDS)
                    .subscribe({ `val` -> finish() }) { t -> t.printStackTrace() }
            )

        } else {
            //continue
        }
    }

    companion object {

        fun create(context: Context) {
            val intent = Intent(Intent(context, OfflineActivity::class.java))
            context.startActivity(intent)
        }
    }

    /**
     * disable back press
     */
    override fun onBackPressed() {

    }
}
