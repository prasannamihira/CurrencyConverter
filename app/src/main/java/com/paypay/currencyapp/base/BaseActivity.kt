package com.paypay.currencyapp.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.paypay.currencyapp.ui.currency.CurrencyConverterActivity
import com.paypay.currencyapp.ui.offline.OfflineActivity
import com.paypay.currencyapp.util.AppUtil
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.net.ConnectException
import java.util.concurrent.TimeUnit

open class BaseActivity: AppCompatActivity() {


    protected open val subscription = CompositeDisposable()
    private var mNetworkChangeReceiver: BroadcastReceiver? = null

    /**
     * Activity onStart
     */
    override fun onStart() {
        super.onStart()

        if (needInternetChangeListener()) {
            registerInternetConnectionChangeReceiver()
        }
    }

    private fun needInternetChangeListener(): Boolean {
        return true
    }

    /**
     * @param hasInternet:Boolean
     */
    open fun internetChanged(hasInternet: Boolean) {
        Timber.d("internetChanged :%s", hasInternet)
    }

    open fun needOfflineScreen(): Boolean {
        return true
    }

    /**
     * register broadcast receiver for detect network connectivity
     */
    private fun registerInternetConnectionChangeReceiver() {
        mNetworkChangeReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                Timber.d("onReceive")
                val internet = AppUtil.hasInternet(context)
                if (!internet) {
                    subscription.add(
                        io.reactivex.Observable.just(1)
                            .subscribeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                            .observeOn(io.reactivex.schedulers.Schedulers.io())
                            .delay(1, TimeUnit.SECONDS)
                            .subscribe({ i ->
                                val hasInternet = AppUtil.hasInternet(context)
                                if (!hasInternet && needOfflineScreen()) {
                                    OfflineActivity.create(this@BaseActivity)
                                }
                                internetChanged(hasInternet)
                            }, { t -> t.printStackTrace() })
                    )
                } else {
                    internetChanged(internet)
                }
            }
        }

        // register network broadcast receiver
        registerReceiver(mNetworkChangeReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    /**
     * Hide the status bar
     */
    fun hideStatusBar() {

        //Hide the status bar on Android 4.0 and Lower
        if (Build.VERSION.SDK_INT < 16) {

            requestWindowFeature(Window.FEATURE_NO_TITLE)

            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            val decorView = window.decorView
            // Hide the status bar.
            val visibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = visibility
            // Hide action bar that too if necessary.
            val actionBar = actionBar
            actionBar?.hide()
        }
    }

    fun doAnimate(root: ViewGroup) {
        TransitionManager.beginDelayedTransition(root)
    }

    /**
     * progress bar
     * @param view:ProgressBar
     * @param show:Boolean
     */
    fun showProgress(view: ProgressBar, show: Boolean) {

        if (show)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.INVISIBLE
    }

    /**
     * Activity onStop
     */
    override fun onStop() {
        super.onStop()
        subscription.clear()
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
    }

    /**
     * Show toast message
     *
     * @param msg:String
     */
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * show alert message
     */
    fun showAlertOK(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }

    /**
     * handle network error
     */
    fun handleNetworkError(e: Throwable) {
        e.printStackTrace()
        if (e is ConnectException) {
            showToast("Please connect to Internet")
        } else {
            showToast("Please try again")
        }
    }

    /**
     * Start currency converter Activity
     */
    fun startCurrencyConverter() {
        val intent = Intent(this, CurrencyConverterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}