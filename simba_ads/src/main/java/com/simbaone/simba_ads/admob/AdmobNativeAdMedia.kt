package com.simbaone.simba_ads.admob

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_TOP_RIGHT
import com.google.android.gms.ads.nativead.*
import com.simbaone.simba_ads.R
import com.simbaone.simba_ads.utils.SmUtils
import com.simbaone.simba_ads.utils.getcolor
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class AdmobNativeAdMedia(private val context: Context, private val attrs: AttributeSet) :
    FrameLayout(context, attrs), LifecycleObserver {

    val TAG = "AdmobNativeAdSetup"

    private var mTextColor: Int = context.getcolor(R.color.sm_colorWhite)
    private var mLayout: Int = R.layout.sm_admob_native_ad_media

    private lateinit var adView: NativeAdView

    private var isDestroyed = false

    private lateinit var adLoader: AdLoader
    private lateinit var nativeAd: NativeAd
    private var shimmerView: ShimmerFrameLayout? = null


    init {
        (context as LifecycleOwner).lifecycle.addObserver(this)

        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AdmobNativeAdMedia, 0, 0
        ).apply {

            try {
                mTextColor = getColor(
                    R.styleable.AdmobNativeAdMedia_mTextColor,
                    context.getcolor(R.color.sm_colorWhite)
                )
                /*mLayout =
                    getResourceId(
                        R.styleable.AdmobNativeAdMedia_mLayout,
                        R.layout.native_ad_media
                    )*/
            } finally {
                recycle()
            }
        }
        initAdd()
    }

    private fun initAdd() {
        if (SmUtils.isConnected(context)) {
            adView = LayoutInflater.from(context).inflate(mLayout, null) as NativeAdView
            shimmerView = adView.findViewById(R.id.shimmerView)

            shimmerView?.startShimmer()
            visibility = VISIBLE
            removeAllViews()
            addView(adView)

            var adId = ""
            context.theme
                .obtainStyledAttributes(
                    attrs,
                    R.styleable.AdmobNativeAdMedia, 0, 0
                )
                .apply {
                    adId = getString(R.styleable.AdmobNativeAdMedia_AD_id).orEmpty()
                }

            requireNotEmpty(adId) { "Ad Id cannot be empty" }

            loadAdmobNativeAd(adId)
        } else {
            visibility = GONE
        }
    }

    @OptIn(ExperimentalContracts::class)
    inline fun requireNotEmpty(value: String?, lazyMessage: () -> String): String {
        contract {
            returns() implies (value != null)
        }

        if (value == null) {
            val message = lazyMessage()
            throw IllegalArgumentException(message)
        } else {
            return value
        }
    }


    @SuppressLint("MissingPermission")
    private fun loadAdmobNativeAd(adId: String) {

        val videoOptionBuilder = VideoOptions.Builder()
            .setClickToExpandRequested(true)
            .setStartMuted(true)

        val nativeAdOptionsBuilder = NativeAdOptions.Builder()
            .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
            .setVideoOptions(videoOptionBuilder.build())

        adLoader = AdLoader.Builder(context, adId)
            .forNativeAd { ad: NativeAd ->
                // Show the ad.
                nativeAd = ad

                if (isDestroyed) {
                    nativeAd.destroy()
                    return@forNativeAd
                }

                if (adLoader.isLoading) {
                    // The AdLoader is still loading ads.
                    // Expect more adLoaded or onAdFailedToLoad callbacks.
                } else {
                    // The AdLoader has finished loading ads.
                    createNativeAd()
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adView.visibility = View.GONE
                    Log.e("NativeAd Error", adError.message)
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(nativeAdOptionsBuilder.build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun createNativeAd() {
        shimmerView?.hideShimmer()
        visibility = VISIBLE
        populateAdmobNativeAd()
    }

    private fun populateAdmobNativeAd() {

        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
        if (headlineView != null) {
//            headlineView.setTextColor(mTextColor)
            headlineView.text = nativeAd.headline
            adView.headlineView = headlineView
        }

        val bodyView = adView.findViewById<TextView>(R.id.ad_body)
        if (bodyView != null) {
//            bodyView.setTextColor(mTextColor)
            bodyView.text = nativeAd.body
            adView.bodyView = bodyView
        }

        val adChoiceView = adView.findViewById<AdChoicesView>(R.id.adChoiceView)
        if (adChoiceView != null) {
            adView.adChoicesView = adChoiceView
        }

        val adPrice = adView.findViewById<TextView>(R.id.ad_price)
        if (adPrice != null) {
//            adPrice.setTextColor(mTextColor)
            adPrice.text = nativeAd.price
            adView.priceView = adPrice
        }

        val adStore = adView.findViewById<TextView>(R.id.ad_store)
        if (adStore != null) {
//            adStore.setTextColor(mTextColor)
            adStore.text = nativeAd.store
            adView.storeView = adStore
        }

        val callToActionView = adView.findViewById<TextView>(R.id.ad_call_to_action)
        if (callToActionView != null) {
            callToActionView.text = nativeAd.callToAction
            adView.callToActionView = callToActionView
        }

        val adAppIcon = adView.findViewById<ImageView>(R.id.ad_app_icon)
        if (adAppIcon != null && nativeAd.icon != null) {
            adAppIcon.setImageDrawable(nativeAd.icon!!.drawable)
            adView.iconView = adAppIcon
        }

        val adStars = adView.findViewById<RatingBar>(R.id.ad_stars)
        if (adStars != null && nativeAd.starRating != null) {
            adStars.rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView = adStars
        }

        val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
        if (advertiser != null) {
            advertiser.text = nativeAd.advertiser
            adView.advertiserView = advertiser
        }

        try {
            val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
            if (mediaView != null) {
                mediaView.visibility = View.VISIBLE
                adView.mediaView = mediaView
                nativeAd.mediaContent?.let {
                    mediaView.setMediaContent(it)
                }
                mediaView.setImageScaleType(ImageView.ScaleType.FIT_CENTER)
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            Logger.logFirebaseEvent("Native Ad Exception $e")
        }

        adView.setNativeAd(nativeAd)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.e(TAG, "Native Ad Resume")
        initAdd()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        isDestroyed = true
        Log.e(TAG, "Native Ad Destroy")
    }
}