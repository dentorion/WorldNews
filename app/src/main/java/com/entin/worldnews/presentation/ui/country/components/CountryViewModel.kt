package com.entin.worldnews.presentation.ui.country.components

import androidx.lifecycle.viewModelScope
import com.entin.worldnews.NavgraphDirections
import com.entin.worldnews.domain.model.*
import com.entin.worldnews.domain.usecase.DeleteNewsByCountryUseCase
import com.entin.worldnews.domain.usecase.GetNewsByCountryUseCase
import com.entin.worldnews.domain.usecase.GetNewsOfflineUseCase
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.base.viewmodel.LiveResult
import com.entin.worldnews.presentation.base.viewmodel.MutableLiveResult
import com.entin.worldnews.presentation.navigation.NavManager
import com.entin.worldnews.presentation.ui.dialogs.delete.DeleteFinishedDialogDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Country ViewModel
 * parent - [BaseViewModel] uses in BaseFragment
 */

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val deleteNewsByCountryUseCase: DeleteNewsByCountryUseCase,
    private val getNewsByCountryUseCase: GetNewsByCountryUseCase,
    private val getNewsOfflineUseCase: GetNewsOfflineUseCase,
    private val navManager: NavManager,
) : BaseViewModel() {

    /**
     * Ui State
     * with typealias: LiveResult<T> = LiveData<ViewModelResult<T>>
     */
    private val _stateScreen = MutableLiveResult<CountryViewState>(PendingResult())
    val stateScreen: LiveResult<CountryViewState> = _stateScreen

    // Current country viewModel working with
    private lateinit var currentCountry: Country

    // Current topic of news
    var stateOfNewsTopic: NewsTopic = NewsTopic.All
        private set

    /**
     * Repeat button behavior in a case of Fail Result
     */
    override fun onRepeat() {
        loadData(currentCountry, isForced = true)
    }

    /**
     * Loading news
     */
    fun loadData(currentCountry: Country, isForced: Boolean = false) {
        _stateScreen.value = PendingResult()
        setCurrentCountry(currentCountry)
        getNewsList(isForced)
    }

    /**
     * Get previously saved news from database
     */
    fun getOfflineNews(country: Country) = viewModelScope.launch {
        getNewsOfflineUseCase.invoke(country).collect { result ->
            onUseCaseResult(result, false)
        }
    }

    /**
     * Set new topic of interesting news
     */
    fun setNewsTopic(topic: NewsTopic) {
        stateOfNewsTopic = topic
        loadData(currentCountry)
    }

    /**
     * Swipe to refresh
     */
    fun onSwipeGuest(country: Country) = viewModelScope.launch {
        loadData(country, true)
    }

    /**
     * Delete all news by country
     */
    fun deleteNewsByCountry(country: Country) = viewModelScope.launch {
        deleteNewsByCountryUseCase(country)
        navManager.navigate(DeleteFinishedDialogDirections.actionGlobalNewsDeleted())
    }

    /**
     * Navigation
     */
    fun navigateToArticleDetails(article: Article) {
        val navDirections = NavgraphDirections.actionGlobalDetailFragment(article)
        navManager.navigate(navDirections)
    }

    fun navigateToFavourite() {
        val navDirections = NavgraphDirections.actionGlobalFavouriteNewsFragment()
        navManager.navigate(navDirections)
    }

    fun navigateToSearch() {
        val navDirections = NavgraphDirections.actionGlobalSearchFragment()
        navManager.navigate(navDirections)
    }

    /**
     * Set current country
     */
    private fun setCurrentCountry(country: Country) {
        currentCountry = country
    }

    /**
     * Request news in UseCase and react on response
     */
    private fun getNewsList(isForced: Boolean = false) =
        viewModelScope.launch {
            getNewsByCountryUseCase(currentCountry, isForced).collect { result ->
                onUseCaseResult(result, isForced)
            }
        }

    /**
     * On UseCase Result
     */
    private fun onUseCaseResult(
        result: UseCaseResult,
        isForced: Boolean
    ) {
        when (result) {
            is UseCaseResult.Success -> {
                onSuccess(result, isForced)
            }
            is UseCaseResult.Error -> {
                onError(result)
            }
            is UseCaseResult.Empty -> {
                onEmpty(isForced)
            }
        }
    }

    /**
     * On UseCase Result.Success
     */
    private fun onSuccess(
        result: UseCaseResult.Success,
        isForced: Boolean
    ) {
        _stateScreen.postValue(
            SuccessResult(
                CountryViewState(
                    news = result.data,
                    isForced = isForced,
                )
            )
        )
    }

    /**
     * On UseCase Result.Empty
     */
    private fun onEmpty(isForced: Boolean) {
        _stateScreen.postValue(
            SuccessResult(
                CountryViewState(
                    isForced = isForced,
                    isEmpty = true,
                )
            )
        )
    }

    /**
     * On UseCase Result.Error
     */
    private fun onError(result: UseCaseResult.Error) {
        _stateScreen.postValue(
            ErrorResult(
                CountryViewState(
                    exception = result.e,
                    exceptionMessage = ExceptionMessage.NoInternet
                )
            )
        )
    }
}