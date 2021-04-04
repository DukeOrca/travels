package com.duke.orca.android.kotlin.travels.application

import android.app.Application
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.util.printHashKey
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this, getString(R.string.native_app_key))
    }
}