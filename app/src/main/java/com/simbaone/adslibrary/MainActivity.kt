package com.simbaone.adslibrary

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.core.animation.doOnEnd
import com.google.android.gms.ads.FullScreenContentCallback
import com.simbaone.SimbaAds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.showInterstitial).apply {
            ObjectAnimator.ofFloat(this, "translationY", 100f).apply {
                duration = 3000
                interpolator = LinearInterpolator()
                // Using KTX extension function:
                doOnEnd { /** Do whatever you want **/ }
                start()
            }
        }

        findViewById<Button>(R.id.showInterstitial).setOnClickListener {
            SimbaAds.showInterstitialAd(
                activity = this@MainActivity,
                onAdDismissed = {
                    Log.e("MainActivity", "onAdDismissed")
                },
                onAdClicked = {
                    Log.e("MainActivity", "onAdClicked")
                },
                onAdImpression = {
                    Log.e("MainActivity", "onAdImpression")
                },
                onAdShowed = {
                    Log.e("MainActivity", "onAdShowed")
                }
            )
        }
    }
}