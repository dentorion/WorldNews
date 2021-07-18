package com.example.worldnews.data.datasource.di

import com.example.db.dao.NewsDAO
import com.example.network.api.NewsApiService
import com.example.worldnews.data.datasource.local.LocalDataSource
import com.example.worldnews.data.datasource.local.LocalDataSourceImpl
import com.example.worldnews.data.datasource.remote.RemoteDataSource
import com.example.worldnews.data.datasource.remote.RemoteDataSourceImpl
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