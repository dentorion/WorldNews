package com.entin.worldnews.presentation.util.connection

import android.content.Context
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
        ConnectionCheckLiveData(context = context)
}