package com.simbaone.simba_ads.abstract

import android.app.Activity
import android.content.Context

abstract class IntersAd {

    abstract val TAG: String

    lateinit var adListener: AdListener

    abstract fun show(activity: Activity)

    abstract fun load(context: Context)

    fun adListener(adListener: AdListener)  {
        this.adListener = adListener
    }

    interface AdListener {
        fun onAdClicked() {

        }

        fun onAdDismissed() {

        }

        fun onAdFailedToShow(p0: AdError) {

        }

        fun onAdImpression() {

        }

        fun onAdShowed() {

        }
    }

    data class AdError(
        val error: String,
        val code: Int
    )
}

public inline fun IntersAd.addListener(
    crossinline onAdClicked: () -> Unit = {},
    crossinline onAdDismissed: () -> Unit = {},
    crossinline onAdFailedToShow: (adError: IntersAd.AdError) -> Unit = {},
    crossinline onAdImpression: () -> Unit = {},
    crossinline onAdShowed: () -> Unit = {}
): IntersAd.AdListener {
    val listener = object : IntersAd.AdListener {
        override fun onAdClicked() = onAdClicked()
        override fun onAdDismissed() = onAdDismissed()
        override fun onAdFailedToShow(p0: IntersAd.AdError) = onAdFailedToShow(p0)
        override fun onAdImpression() = onAdImpression()
        override fun onAdShowed() = onAdShowed()
    }
    adListener(listener)
    return listener
}