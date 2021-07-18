package com.example.worldnews.presentation.util

import android.content.Context
import android.content.SharedPreferences
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.presentation.extension.get
import com.example.worldnews.presentation.extension.set
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

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