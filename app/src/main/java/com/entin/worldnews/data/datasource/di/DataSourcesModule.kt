package com.entin.worldnews.data.datasource.di

import com.entin.db.dao.NewsDAO
import com.entin.network.api.NewsApiService
import com.entin.worldnews.data.datasource.local.LocalDataSource
import com.entin.worldnews.data.datasource.local.LocalDataSourceImpl
import com.entin.worldnews.data.datasource.remote.RemoteDataSource
import com.entin.worldnews.data.datasource.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(newsDAO: NewsDAO): LocalDataSource =
        LocalDataSourceImpl(newsDAO)

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: NewsApiService): RemoteDataSource =
        RemoteDataSourceImpl(apiService)
}