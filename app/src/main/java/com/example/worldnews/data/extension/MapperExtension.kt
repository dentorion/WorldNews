package com.example.worldnews.data.extension

import com.example.db.entity.ArticleRoom
import com.example.network.entity.ArticleJson
import com.example.worldnews.domain.entity.Article

fun ArticleJson.toDbModel() =
    ArticleRoom(
        author = this.author,
        content = this.content,
        description = this.description,
        publishedAt = this.publishedAt,
        title = this.title,
        url = this.url,
        urlToImage = this.urlToImage
    )

fun ArticleJson.toDomainModel() = Article(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage
)

fun ArticleRoom.toDomainModel() = Article(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage,
    shown = this.shown
)