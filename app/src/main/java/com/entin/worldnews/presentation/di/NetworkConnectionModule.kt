package com.entin.worldnews.presentation.di

import android.content.Context
import com.entin.worldnews.presentation.util.ConnectionLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConnectionModule {

    @Provides
    @Singleton
    fun provideNetworkCheck(@ApplicationContext context: Context) =
        ConnectionLiveData(context = context)
}