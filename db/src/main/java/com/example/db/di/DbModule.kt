package com.example.db.di

import android.content.Context
import androidx.room.Room
import com.example.db.Db
import com.example.db.dao.NewsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): Db =
        Room.databaseBuilder(
            context,
            Db::class.java,
            Db.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDAO(db: Db): NewsDAO = db.newsDao()
}