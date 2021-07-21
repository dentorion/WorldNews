package com.example.worldnews.presentation.application

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import com.example.worldnews.presentation.util.NotificationWorkManager
import com.example.worldnews.presentation.util.WORK_TAG
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application() {

}