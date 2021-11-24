package com.entin.worldnews.data.extension

import com.entin.db.entity.ArticleRoomModel
import com.entin.network.model.ArticleJson
import com.entin.worldnews.domain.model.Article

fun ArticleJson.toDbModel() =
    ArticleRoomModel(
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

fun ArticleRoomModel.toDomainModel() = Article(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage,
    shown = this.shown
)

fun Article.toDbModel() = ArticleRoomModel(
    url = this.url,
    title = this.title,
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    urlToImage = this.urlToImage
)