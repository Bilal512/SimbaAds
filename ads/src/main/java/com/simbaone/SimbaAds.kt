package com.simbaone

import android.app.Activity
import android.app.Application
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.RequestConfiguration
import com.simbaone.simba_ads.AdNetwork
import com.simbaone.simba_ads.SmSession
import com.simbaone.simba_ads.abstract.IntersAd
import com.simbaone.simba_ads.abstract.addListener
import com.simbaone.simba_ads.admob.AdmobIntersAdImpl
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class SimbaAds private constructor(
    context: Application,
    testId: String,
    admobInterstitalAdId: String,
    admobAppOpenId: String,
    adNetwork: AdNetwork,
) {

    class Builder {
        private lateinit var context: Application
        private var testDeviceId: String = ""
        private var isDebug: Boolean = false
        private var admobInterstitialAdId: String = ""
        private var admobAppOpenId: String = ""
        private var adNetwork: AdNetwork = AdNetwork.ADMOB

        fun setTestDeviceId(testDeviceId: String): Builder {
            this.testDeviceId = testDeviceId
            return this
        }

        fun setIsDebug(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun setAdmobInterstitialAdId(admobInterstitialAdId: String): Builder {
            this.admobInterstitialAdId = admobInterstitialAdId
            return this
        }

        fun setAdmobAppOpenId(admobAppOpenId: String): Builder {
            this.admobAppOpenId = admobAppOpenId
            return this
        }

        fun setAdNetwork(adNetwork: AdNetwork): Builder {
            this.adNetwork = adNetwork
            return this
        }

        fun build(context: Application): SimbaAds {
            SmSession.IS_DEBUG = isDebug
            return SimbaAds(
                context = context,
                testId = testDeviceId,
                admobInterstitalAdId = admobInterstitialAdId,
                admobAppOpenId = admobAppOpenId,
                adNetwork = adNetwork
            )
        }
    }


    companion object {
        fun builder(): Builder {
            return Builder()
        }

        private val intersAd: IntersAd by lazy {
            return@lazy when (SmSession.adNetwork) {
                AdNetwork.ADMOB -> AdmobIntersAdImpl()
                AdNetwork.FACEBOOK -> AdmobIntersAdImpl()
            }
        }

        fun showInterstitialAd(
            activity: Activity,
            onAdClicked: () -> Unit = {},
            onAdDismissed: () -> Unit = {},
            onAdFailedToShow: (adError: IntersAd.AdError) -> Unit = {},
            onAdImpression: () -> Unit = {},
            onAdShowed: () -> Unit = {}
        ) {
            checkLibrary()
            intersAd.addListener(
                onAdClicked = onAdClicked,
                onAdDismissed = {
                    onAdDismissed()
                    intersAd.load(activity)
                },
                onAdFailedToShow = onAdFailedToShow,
                onAdImpression = onAdImpression,
                onAdShowed = onAdShowed
            )
            intersAd.show(activity)
        }

        @JvmStatic
        fun showInterstitialAd(
            activity: Activity,
            adListener: IntersAd.AdListener
        ) {
            checkLibrary()
            intersAd.adListener(adListener)
            intersAd.show(activity)
        }
    }

    init {
        RequestConfiguration.Builder().setTestDeviceIds(listOf(testId))
        SmSession.admobInterstitalAdId = admobInterstitalAdId
        SmSession.admobAppOpenId = admobAppOpenId
        SmSession.isLibraryInitialized = true
        SmSession.adNetwork = adNetwork
        intersAd.load(context)
    }
}

fun checkLibrary() {
    requireLibraryInitialized(SmSession.isLibraryInitialized) { "Simba_ads library not initialized" }
}

@OptIn(ExperimentalContracts::class)
inline fun requireLibraryInitialized(flag: Boolean, lazyMessage: () -> String): Boolean {
    contract {
        returns() implies (flag)
    }

    if (SmSession.isLibraryInitialized.not()) {
        val message = lazyMessage()
        throw IllegalArgumentException(message)
    } else {
        return SmSession.isLibraryInitialized
    }
}
