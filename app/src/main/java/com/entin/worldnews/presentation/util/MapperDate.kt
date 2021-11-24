package com.entin.worldnews.presentation.util

object MapperDate {

    fun cropPublishedAtToDate(str: String): String {
        return str.split("T")[0]
    }

    fun cropPublishedAtToTime(str: String): String {
        val a = str.split("Z")[0]
        val b = a.split("T")[1]
        return b.dropLast(3)
    }
}