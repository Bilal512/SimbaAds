package com.simbaone.simba_ads.facebook

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.simbaone.R
import com.simbaone.simba_ads.utils.SmUtils

class FacebookBanner(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.sm_admob_banner_layout, this, true)
        val adContainer: FrameLayout = view.findViewById(R.id.sm_ad_view_container)

        var adId = ""
        context.theme
            .obtainStyledAttributes(
                attrs,
                R.styleable.AdmobNativeAdMedia, 0, 0
            )
            .apply {
                adId = getString(R.styleable.AdmobBanner_AD_id).orEmpty()
            }


        val size: AdSize = if (SmUtils.isTablet(context)) {
            AdSize.BANNER_HEIGHT_90
        } else {
            AdSize.BANNER_HEIGHT_50
        }
        val adView = AdView(context, adId, size)
        adContainer.addView(adView)
        adView.loadAd()
    }
}