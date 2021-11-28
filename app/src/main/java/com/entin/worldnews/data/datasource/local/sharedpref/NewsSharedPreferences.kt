package com.entin.worldnews.data.datasource.local.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.presentation.extension.get
import com.entin.worldnews.presentation.extension.set
import com.entin.worldnews.presentation.util.NEWS_SHARED_PREFERENCES_TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsSharedPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(NEWS_SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)

    fun getLastTimeDownloaded(country: Country): Long =
        pref.getLong(country.countryName, Long.MIN_VALUE)

    fun wasDownloaded(country: Country): Boolean {
        if (pref[country.countryName, Long.MIN_VALUE] == Long.MIN_VALUE) return false
        return true
    }

    fun clearLastDownload(country: Country) {
        pref[country.countryName] = Long.MIN_VALUE
    }

    fun addCurrentTimeByCountry(country: Country) {
        pref[country.countryName] = System.currentTimeMillis()
    }
}