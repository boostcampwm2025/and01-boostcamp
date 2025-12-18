package com.andone.memorip

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MemoripApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}