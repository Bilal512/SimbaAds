package com.simbaone

import android.app.Application
import com.simbaone.adslibrary.BuildConfig
import com.simbaone.simba_ads.AdNetwork

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        SimbaAds
            .builder()
            .setIsDebug(BuildConfig.DEBUG)
            .setTestDeviceId("9257EDC2AC708B3D76BD11D96DEA6FD4")
            .setAdmobAppOpenId("")
            .setTestDeviceId("")
            .setAdNetwork(AdNetwork.ADMOB)
            .build(this)
    }
}