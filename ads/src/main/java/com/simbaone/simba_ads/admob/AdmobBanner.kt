package com.simbaone.simba_ads.admob

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.simbaone.checkLibrary
import com.simbaone.R

class AdmobBanner(
    private val context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private lateinit var adView: AdView

    init {
        Log.e("Banner", "init")
        checkLibrary()
        if (!isInEditMode) {

            var adId = ""

            context.theme
                .obtainStyledAttributes(
                    attrs,
                    R.styleable.AdmobBanner, 0, 0
                )
                .apply {
                    adId = AdmobIdUtils.processAdId(
                        getString(R.styleable.AdmobBanner_AD_id).orEmpty(),
                        ADMOB_ADS.BANNER
                    )
                }

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.sm_admob_banner_layout, this, true)
            val adContainerView = view.findViewById<FrameLayout>(R.id.sm_ad_view_container)
            // Step 1 - Create an AdView and set the ad unit ID on it.
            adView = AdView(context)
            //        adView.setAdUnitId(getResources().getString(R.string.banner_id));
            adView.adUnitId = adId
            adContainerView.addView(adView)
            loadBanner()
        }
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        val builder = AdRequest.Builder()
        val adRequest = builder.build()
        val adSize = adSize
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize)

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.e("Banner", "Banner Ad Loaded")
            }
        }

    }

    // Step 2 - Determine the screen width (less decorations) to use for the ad width.
    private val adSize: AdSize
        @SuppressLint("VisibleForTests")
        get() {
            // Step 2 - Determine the screen width (less decorations) to use for the ad width.
            val display = (context as Activity?)!!.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density
            val adWidth = (widthPixels / density).toInt()

            // Step 3 - Get adaptive ad size and return for setting on the ad view.
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                context, adWidth
            )
        }
}