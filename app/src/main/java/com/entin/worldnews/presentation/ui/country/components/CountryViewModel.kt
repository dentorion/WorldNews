package com.entin.worldnews.presentation.ui.country.components

import androidx.lifecycle.viewModelScope
import com.entin.worldnews.NavgraphDirections
import com.entin.worldnews.domain.model.*
import com.entin.worldnews.domain.usecase.CheckLastTimeDownloadUseCase
import com.entin.worldnews.domain.usecase.DeleteNewsByCountryUseCase
import com.entin.worldnews.domain.usecase.GetNewsByCountryUseCase
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
    private val checkLastTimeDownloadUseCase: CheckLastTimeDownloadUseCase,
    private val navManager: NavManager,
) : BaseViewModel() {

    /**
     * Ui State
     * with typealias: LiveResult<T> = LiveData<WorldNewsResult<T>>
     */
    private val _stateScreen = MutableLiveResult<CountryViewState>(PendingResult())
    val stateScreen: LiveResult<CountryViewState> = _stateScreen

    /**
     * Current country viewModel working with
     */
    private lateinit var currentCountry: Country

    /**
     * Current topic of news
     */
    var stateOfNewsTopic: NewsTopic = NewsTopic.All
        private set

    /**
     * Loading news
     */
    fun loadData(currentCountry: Country, isForced: Boolean = false) {
        setCurrentCountry(currentCountry)
        getNewsList(isForced)
    }

    /**
     * Set new topic of interesting news
     */
    fun setNewsTopic(topic: NewsTopic) {
        stateOfNewsTopic = topic
        loadData(currentCountry)
    }

    /**
     * On swipe guest reaction: check is last time downloading less than 2 hours from now.
     * If yes -> this.loadData() / no -> Toast about zero updates (server has rare updates).
     */
    fun onSwipeGuest(country: Country) = viewModelScope.launch {
        if (checkLastTimeDownloadUseCase(country)) {
            loadData(country)
        } else {
            _stateScreen.postValue(
                stateScreen.value.takeSuccessOnly()?.let {
                    SuccessResult(it.copy(isSame = true))
                }
            )
        }
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
    private fun getNewsList(isForced: Boolean) {
        viewModelScope.launch {
            getNewsByCountryUseCase(currentCountry, isForced).collect { result ->
                when (result) {
                    is UseCaseResult.Success -> {
                        _stateScreen.postValue(
                            SuccessResult(CountryViewState(news = result.data, isForced = isForced))
                        )
                    }
                    is UseCaseResult.Error -> {
                        _stateScreen.postValue(
                            ErrorResult(result.e.message.toString())
                        )
                    }
                    is UseCaseResult.Empty -> {
                        _stateScreen.postValue(
                            SuccessResult(CountryViewState(isForced = isForced))
                        )
                    }
                }
            }
        }
    }
}

/**
 * Inside Ui State of Country Fragment
 */
data class CountryViewState(
    val message: String = "",
    val news: List<Article> = listOf(),
    val isForced: Boolean = false,
    val isSame: Boolean = false,
)