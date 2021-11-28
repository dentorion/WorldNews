package com.entin.worldnews.domain.model

/**
 * News topic state
 * Used in CountryFragment with CountryViewModel
 */

sealed class NewsTopic {
    object All : NewsTopic()
    object General : NewsTopic()
    object Health : NewsTopic()
    object Sports : NewsTopic()
}