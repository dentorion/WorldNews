package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.dao.NewsDAO
import com.example.db.entity.ArticleRoom

@Database(entities = [ArticleRoom::class], version = 1, exportSchema = false)
abstract class Db: RoomDatabase() {

    abstract fun newsDao(): NewsDAO

    companion object {
        const val DATABASE_NAME: String = "NewsDatabase"
    }
}