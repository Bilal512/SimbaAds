package com.simbaone.simba_ads.admob

import com.simbaone.simba_ads.SmSession

object AdmobIdUtils {

    fun processAdId(id: String, type: ADMOB_ADS): String {
        return when(type) {
            ADMOB_ADS.BANNER -> if(SmSession.IS_DEBUG) AdmobTestIds.Banner else id
            ADMOB_ADS.NATIVE -> if(SmSession.IS_DEBUG) AdmobTestIds.Native_Advanced else id
            ADMOB_ADS.INTERSTITIAL -> if(SmSession.IS_DEBUG) AdmobTestIds.Interstitial else id
            ADMOB_ADS.APP_OPEN -> if(SmSession.IS_DEBUG) AdmobTestIds.App_Open else id
        }
    }


}

enum class ADMOB_ADS {
    BANNER,
    NATIVE,
    INTERSTITIAL,
    APP_OPEN
}
