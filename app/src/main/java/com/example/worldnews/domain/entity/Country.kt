package com.example.worldnews.domain.entity

sealed class Country(val countryName: String) {
    object Usa : Country("us")
    object Poland : Country("pl")
    object Ukraine : Country("ua")
    object Russia : Country("ru")
}