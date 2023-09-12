package com.simbaone

import android.app.Application
import com.simbaone.adslibrary.BuildConfig

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        SimbaAds
            .builder()
            .setTestDeviceId("9257EDC2AC708B3D76BD11D96DEA6FD4")
            .setIsDebug(BuildConfig.DEBUG)
            .build(this)
    }
}