package com.entin.worldnews.presentation.di

import android.content.Context
import com.entin.worldnews.presentation.util.NewsSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): NewsSharedPreferences =
        NewsSharedPreferences(context = context)
}