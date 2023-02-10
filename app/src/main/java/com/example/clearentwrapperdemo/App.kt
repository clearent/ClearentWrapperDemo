package com.example.clearentwrapperdemo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.clearent.idtech.android.wrapper.ClearentCredentials
import com.clearent.idtech.android.wrapper.ClearentWrapper

class App : Application() {

    private val clearentWrapper = ClearentWrapper.getInstance()

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // We initialize the sdk
        initSdk()
    }

    private fun initSdk() {
        clearentWrapper.sdkCredentials.clearentCredentials =
            ClearentCredentials.ApiCredentials(Constants.API_KEY_SANDBOX)
        clearentWrapper.sdkCredentials.publicKey = Constants.PUBLIC_KEY_SANDBOX

        clearentWrapper.initializeSDK(
            applicationContext,
            Constants.BASE_URL_SANDBOX,
        )
    }
}