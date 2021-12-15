package com.entin.worldnews.presentation.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.entin.worldnews.domain.model.ViewModelResult

/**
 * For prevent writing all the time <WorldNewsResult<T>> in CountryViewModel
 */
typealias LiveResult<T> = LiveData<ViewModelResult<T>>
typealias MutableLiveResult<T> = MutableLiveData<ViewModelResult<T>>

/**
 * Base ViewModel
 */

abstract class BaseViewModel : ViewModel() {

    abstract fun onRepeat()
}