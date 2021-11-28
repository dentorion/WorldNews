package com.entin.worldnews.data.extension

import com.entin.db.entity.ArticleRoom
import com.entin.network.model.ArticleJson
import com.entin.worldnews.domain.model.Article

/**
 * Article Server Response -> Article Room model
 */
fun ArticleJson.toDbModel() =
    ArticleRoom(
        author = this.author,
        content = this.content,
        description = this.description,
        publishedAt = this.publishedAt,
        title = this.title,
        url = this.url,
        urlToImage = this.urlToImage,
        category = this.category,
    )

/**
 * Article Server Response -> Article Domain model
 */
fun ArticleJson.toDomainModel() = Article(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage,
    category = this.category,
)

/**
 * Article Room model -> Article Domain model
 */
fun ArticleRoom.toDomainModel() = Article(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage,
    shown = this.shown,
    category = this.category,
)

/**
 * Article Domain model -> Article Room model
 */
fun Article.toDbModel() = ArticleRoom(
    url = this.url,
    title = this.title,
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    urlToImage = this.urlToImage,
    category = this.category,
)