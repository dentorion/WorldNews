package com.example.db.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "news")
@Parcelize
data class ArticleRoom(

    val author: String?,

    val content: String?,

    val description: String?,

    val publishedAt: String,

    val title: String?,

    @PrimaryKey
    @NonNull
    val url: String,

    val urlToImage: String?

) : Parcelable {

    @IgnoredOnParcel
    var favourite: Boolean = false

    @IgnoredOnParcel
    var category: String = ""

    @IgnoredOnParcel
    var shown: Boolean = false

    @IgnoredOnParcel
    var country: String = ""

}

