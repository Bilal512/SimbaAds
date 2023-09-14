package com.simbaone.simba_ads.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getcolor(@ColorRes id: Int) = ContextCompat.getColor(this, id)