package com.simbaone.simba_ads

import android.view.View

var clickCounter = 0
fun View.onClick(action: () -> Unit) {
    this.setOnClickListener {
        clickCounter++
        if(clickCounter == 3) {
            clickCounter = 0
            //show interstitial Ad
        }
        action()
    }
}