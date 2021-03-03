package com.cairocartt

import android.app.Application
import android.os.Build
import com.cairocartt.BuildConfig.BASE_URL
import com.cairocartt.utils.Constants
import com.google.android.gms.security.ProviderInstaller
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App :Application(){

//    companion object {
//        var  mPrefs: SharedPreferences?=null
//        var  sInstance : App ?=null
//
//    }
    @Suppress("UNUSED_VARIABLE")
    override fun onCreate() {
        super.onCreate()
        // you can use this instance for DI or get it via Lingver.getInstance() later on
        val lingver = Lingver.init(this, ChangeLanguage.getLanguage(this))

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        // only for gingerbread and newer versions
        ProviderInstaller.installIfNeeded(this);

    }

    }
}