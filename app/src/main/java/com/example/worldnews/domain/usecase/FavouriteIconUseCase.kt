package com.example.worldnews.domain.usecase

import com.example.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteIconUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend fun get(url: String): Flow<Boolean> =
        repository.getFavouriteStatusArticle(url = url)

    suspend fun set(url: String) {
        try {
            repository.changeFavouriteArticle(url = url)
        } catch (e: IOException) {
            throw Error("Error on setting news as favourite")
        }
    }
}