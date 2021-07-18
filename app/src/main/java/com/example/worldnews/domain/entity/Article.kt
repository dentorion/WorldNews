package com.example.worldnews.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(

    val author: String?,

    val content: String?,

    val description: String?,

    val publishedAt: String,

    val title: String?,

    val url: String,

    val urlToImage: String?,

    var favourite: Boolean = false,

    var shown: Boolean = false

) : Parcelable
