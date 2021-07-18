package com.example.network.entity

import androidx.annotation.NonNull
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticleJson(
    @Expose
    @SerializedName("author")
    val author: String?,

    @Expose
    @SerializedName("content")
    val content: String?,

    @Expose
    @SerializedName("description")
    val description: String?,

    @Expose
    @SerializedName("publishedAt")
    val publishedAt: String,

    @Expose
    @SerializedName("title")
    val title: String?,

    @Expose
    @SerializedName("url")
    val url: String,

    @Expose
    @SerializedName("urlToImage")
    val urlToImage: String?

) {

    @NonNull
    var id: Int = 0

    var favourite: Boolean = false

    lateinit var category: String

    var shown: Boolean = false

    lateinit var country: String

}