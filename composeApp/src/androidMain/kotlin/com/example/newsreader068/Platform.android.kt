package com.example.newsreader068

import android.os.Build

actual fun getPlatformName(): String = "Android ${Build.VERSION.SDK_INT}"