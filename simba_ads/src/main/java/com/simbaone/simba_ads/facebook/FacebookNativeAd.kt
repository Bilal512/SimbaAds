package com.simbaone.simba_ads.facebook

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.facebook.ads.*
import com.facebook.shimmer.ShimmerFrameLayout
import com.simbaone.simba_ads.R
import com.simbaone.simba_ads.utils.SmUtils

class FacebookNativeAd(private val context: Context, private val attrs: AttributeSet) :
    FrameLayout(context, attrs) {
    val TAG = "FBNativeAd"

    private var nativeAdLayout: NativeAdLayout? = null
    private var adView: ShimmerFrameLayout? = null
    private var minHeight = 0f
    private var mShowMedia = true

    init {
        var adId: String
        context.theme
            .obtainStyledAttributes(
                attrs,
                R.styleable.FacebookNativeAd, 0, 0
            )
            .apply {
                adId = getString(R.styleable.FacebookNativeAd_AD_id).orEmpty()
            }

        val nativeAd = NativeAd(context, adId)
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.FacebookNativeAd)

        minHeight = typedArray.getDimension(R.styleable.FacebookNativeAd_media_min_height, 0f)
        typedArray.recycle()

        val view =
            LayoutInflater.from(context).inflate(R.layout.sm_fb_native_contailer, this, true)
        nativeAdLayout = view.findViewById(R.id.native_ad_container)

        adView = LayoutInflater.from(context)
            .inflate(R.layout.sm_fb_native_lay, nativeAdLayout, false) as ShimmerFrameLayout?
        if (SmUtils.isConnected(context)) {
            if (minHeight > 0) {
                nativeAdLayout?.layoutParams?.height = minHeight.toInt()
            }

            nativeAdLayout?.addView(adView);
        }

        val nativeAdListener = object : NativeAdListener {
            override fun onError(p0: Ad?, p1: AdError?) {
                p1?.let { Log.e(TAG, "onError " + it.errorMessage) }
                visibility = View.GONE
            }

            override fun onAdLoaded(p0: Ad?) {
                // Inflate Native Ad into Container
                Log.e(TAG, "onAdLoaded ")
                inflateAd(nativeAd);
            }

            override fun onAdClicked(p0: Ad?) {
                Log.e(TAG, "onAdClicked")
            }

            override fun onLoggingImpression(p0: Ad?) {

            }

            override fun onMediaDownloaded(p0: Ad?) {

            }
        }

        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("3730075e-52bb-4f0e-8f1f-3cee99d6e295")
        }
        nativeAd.loadAd(
            nativeAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build()
        )
        adView?.startShimmer()
    }

    private fun inflateAd(nativeAd: NativeAd) {
        nativeAd.unregisterView()

        adView?.hideShimmer()


        val adChoicesContainer = adView?.findViewById<LinearLayout>(R.id.ad_choices_container)
        val adOptionView = AdOptionsView(context, nativeAd, nativeAdLayout)
        adChoicesContainer?.removeAllViews()

        adChoicesContainer?.addView(adOptionView, 0)

        //Create Native UI Using the Ad meta data
        val nativeAdIcon: MediaView? = adView?.findViewById(R.id.native_ad_icon)
        val nativeAdTitle: TextView? = adView?.findViewById(R.id.native_ad_title)

        val nativeAdMedia: MediaView? = adView?.findViewById(R.id.native_ad_media)

        val nativeAdSocialContext: TextView? = adView?.findViewById(R.id.native_ad_social_context)
        val nativeAdBody: TextView? = adView?.findViewById(R.id.native_ad_body)
        val sponsoredLabel: TextView? = adView?.findViewById(R.id.native_ad_sponsored_label)
        val nativeAdCallToAction: Button? = adView?.findViewById(R.id.native_ad_call_to_action)

        nativeAdTitle?.text = nativeAd.advertiserName
        nativeAdBody?.text = nativeAd.adBodyText
        nativeAdSocialContext?.text = nativeAd.adSocialContext
        nativeAdCallToAction?.visibility =
            if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdCallToAction?.text = nativeAd.adCallToAction
        sponsoredLabel?.text = nativeAd.sponsoredTranslation

        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle!!)
        clickableViews.add(nativeAdCallToAction!!)

        nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }
}