package com.entin.worldnews.data.datasource.local.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.presentation.extension.set
import com.entin.worldnews.presentation.util.NEWS_SHARED_PREFERENCES_TAG
import com.entin.worldnews.presentation.util.TIME_2HOURS_DOWNLOAD_PAUSE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CacheNewsController helps to control quantity of downloads.
 * Server API limits 100 per day / 50 per 12 hours.
 * CacheNewsController allows downloading not often than once in 2 hours
 */

@Singleton
class CacheNewsController @Inject constructor(@ApplicationContext context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(NEWS_SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)

    /**
     * Returns the boolean:
     * Decision should be: news download from server or database
     *
     * 1. Take last time of downloading
     * 2. If it == Long.MIN_VALUE -> load news from server
     * 3. If it != Long.MIN_VALUE -> check difference between now and last time downloaded
     * 4.                               If it > 2h -> load news from server
     * 5.                               If it < 2h -> load news from database
     */
    fun isAllowDownloadNews(country: Country): Boolean {
        return if (getLastTimeDownload(country) == Long.MIN_VALUE) {
            true
        } else {
            checkTimeDifference(getLastTimeDownload(country))
        }
    }

    // Set last time news were downloading by country
    fun setLastTimeDownload(country: Country) {
        pref[country.countryName] = System.currentTimeMillis()
    }

    // Clear last time news were downloading by country
    fun clearLastTimeDownload(country: Country) {
        pref[country.countryName] = Long.MIN_VALUE
    }

    // Get last time news were downloading by country
    private fun getLastTimeDownload(country: Country): Long =
        pref.getLong(country.countryName, Long.MIN_VALUE)

    // Check difference between last time news were downloading by country and now
    private fun checkTimeDifference(longTime: Long): Boolean {
        if (System.currentTimeMillis().minus(longTime) < TIME_2HOURS_DOWNLOAD_PAUSE) {
            return false
        }
        return true
    }
}