package com.entin.worldnews.domain.model

sealed class Country(val countryName: String) {

    object Usa : Country(USA)

    object Poland : Country(POLAND)

    object Ukraine : Country(UKRAINE)

    object Russia : Country(RUSSIA)
}

private const val USA = "us"
private const val POLAND = "pl"
private const val UKRAINE = "ua"
private const val RUSSIA = "ru"