package com.simbaone.simba_ads.admob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.simbaone.simba_ads.SmSession
import com.simbaone.simba_ads.abstract.IntersAd

class AdmobIntersAdImpl : IntersAd() {

    private var mInterstitialAd: InterstitialAd? = null
    override val TAG = "Admob Inters"

    override fun show(activity: Activity) {
        mInterstitialAd?.fullScreenContentCallback = fullScreenContentCallback
        mInterstitialAd?.show(activity)
    }

    override fun load(context: Context) {
        loadInters(context) {
            mInterstitialAd = it
        }
    }

    private val fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
            adListener.onAdClicked()
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            adListener.onAdDismissed()
        }

        override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            adListener.onAdFailedToShow(IntersAd.AdError(p0.message, p0.code))
        }

        override fun onAdImpression() {
            super.onAdImpression()
            adListener.onAdImpression()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            adListener.onAdShowed()
        }
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest
            .Builder()
            .setHttpTimeoutMillis(3000)
            .build()
    }

    private fun loadInters(context: Context, onLoaded: (interstitial: InterstitialAd?) -> Unit) {
        val intersAdId =
            AdmobIdUtils.processAdId(
                SmSession.admobInterstitalAdId,
                ADMOB_ADS.INTERSTITIAL
            )

        InterstitialAd.load(
            context,
            intersAdId,
            getAdRequest(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    onLoaded.invoke(null)
                    Log.e(TAG, p0.message)
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    Log.e(TAG, "Ad Loaded")
                    onLoaded.invoke(p0)
                }
            }
        )
    }
}