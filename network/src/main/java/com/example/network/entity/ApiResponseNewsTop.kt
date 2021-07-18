package com.example.network.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponseNewsTop(

    @Expose
    @SerializedName("articles")
    val articles: List<ArticleJson>
)