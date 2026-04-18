package com.example.newsreader068

import platform.UIKit.UIDevice

actual fun getPlatformName(): String =
    UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion