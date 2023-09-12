package com.simbaone.simba_ads.facebook

import android.content.Context
import android.util.Log
import com.facebook.ads.*

class FacebookInters(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var onAdLoaded: ((InterstitialAd?) -> Unit)? = null


    fun loadAd(adId: String, onLoad: (InterstitialAd?) -> Unit) {
        interstitialAd = InterstitialAd(context, adId)
        this.onAdLoaded = onLoad


        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("3730075e-52bb-4f0e-8f1f-3cee99d6e295")
        }
        interstitialAd?.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )
    }

    private val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
        override fun onInterstitialDisplayed(ad: Ad) {
            // Interstitial ad displayed callback
            Log.e(Companion.TAG, "Interstitial ad displayed.")
        }

        override fun onInterstitialDismissed(ad: Ad) {
            // Interstitial dismissed callback
            Log.e(Companion.TAG, "Interstitial ad dismissed.")
        }


        override fun onError(ad: Ad?, adError: AdError) {
            // Ad error callback
            Log.e(Companion.TAG, "Interstitial ad failed to load: " + adError.getErrorMessage())
            onAdLoaded?.invoke(null)
        }

        override fun onAdLoaded(ad: Ad) {
            // Interstitial ad is loaded and ready to be displayed
            Log.d(Companion.TAG, "Interstitial ad is loaded and ready to be displayed!")
            // Show the ad
            onAdLoaded?.invoke(interstitialAd!!)
        }

        override fun onAdClicked(ad: Ad) {
            // Ad clicked callback
            Log.d(Companion.TAG, "Interstitial ad clicked!")
        }

        override fun onLoggingImpression(ad: Ad) {
            // Ad impression logged callback
            Log.d(Companion.TAG, "Interstitial ad impression logged!")
        }
    }

    companion object {
        const val TAG = "FacebookInters"
    }
}