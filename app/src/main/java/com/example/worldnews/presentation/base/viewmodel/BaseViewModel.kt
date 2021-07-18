package com.example.worldnews.presentation.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.presentation.extension.asLiveData

abstract class BaseViewModel<ViewState : BaseViewState, ViewAction : BaseAction>() :
    ViewModel() {

    val stateMutableLiveData = MutableLiveData<ViewState>()
    val stateLiveData = stateMutableLiveData.asLiveData()

    abstract var state: ViewState

    fun sendAction(viewAction: ViewAction) {
        state = onReduceState(viewAction)
    }

    fun loadData(country: Country? = null, isForced: Boolean = false) {
        onLoadData(country, isForced)
    }

    open fun onLoadData(country: Country? = null, isForced: Boolean = false) {}

    abstract fun onReduceState(viewAction: ViewAction): ViewState
}
