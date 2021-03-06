package com.entin.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponseNewsTop(

    @Expose
    @SerializedName("articles")
    val articles: List<ArticleJson>
)