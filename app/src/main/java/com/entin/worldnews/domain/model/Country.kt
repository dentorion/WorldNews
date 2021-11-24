package com.entin.worldnews.domain.model

sealed class Country(val countryName: String) {

    object Usa : Country(USA)

    object Poland : Country(POLAND)

    object Ukraine : Country(UKRAINE)

    object Russia : Country(RUSSIA)
}

const val USA = "us"
const val POLAND = "pl"
const val UKRAINE = "ua"
const val RUSSIA = "ru"