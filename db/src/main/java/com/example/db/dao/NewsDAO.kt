package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.db.entity.ArticleRoom
import kotlinx.coroutines.flow.*

@Dao
interface NewsDAO {

    // GET news
    @Query("SELECT * FROM news WHERE country = :country ORDER BY publishedAt DESC")
    fun getNews(country: String): Flow<List<ArticleRoom>>

    // DELETE news
    @Query("DELETE FROM news WHERE country = :country AND favourite != '1'")
    suspend fun deleteNews(country: String)

    // SAVE news
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNews(articles: List<ArticleRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSearchedAndOpenedArticle(articleRoom: ArticleRoom)

    // FAVOURITE NEWS
    @Query("SELECT * FROM news WHERE favourite = '1' ORDER BY publishedAt DESC")
    fun getFavouriteNews(): Flow<List<ArticleRoom>>

    @Query("UPDATE news SET favourite = NOT favourite WHERE url = :url")
    suspend fun changeFavouriteStatusArticle(url: String)

    // SET FAVOURITE
    @Query("UPDATE news SET shown = '1' WHERE url = :url")
    suspend fun setArticleShown(url: String)

    @Query("SELECT favourite FROM news WHERE url = :url ORDER BY publishedAt DESC")
    fun getFavouriteStatusArticle(url: String): Flow<Boolean>

}