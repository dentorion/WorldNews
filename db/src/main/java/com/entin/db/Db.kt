package com.entin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.entin.db.dao.NewsDAO
import com.entin.db.entity.ArticleRoomModel

@Database(entities = [ArticleRoomModel::class], version = 1, exportSchema = false)
abstract class Db: RoomDatabase() {

    abstract fun newsDao(): NewsDAO

    companion object {
        const val DATABASE_NAME: String = "NewsDatabase"
    }
}