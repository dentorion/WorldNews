package com.entin.worldnews.presentation.ui.country.components

import androidx.lifecycle.viewModelScope
import com.entin.worldnews.NavgraphDirections
import com.entin.worldnews.domain.model.*
import com.entin.worldnews.domain.usecase.DeleteNewsUseCase
import com.entin.worldnews.domain.usecase.GetNewsUseCase
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.base.viewmodel.LiveResult
import com.entin.worldnews.presentation.base.viewmodel.MutableLiveResult
import com.entin.worldnews.presentation.navigation.NavManager
import com.entin.worldnews.presentation.ui.dialogs.delete.DeleteFinishedDialogDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Country ViewModel
 * parent - [BaseViewModel] uses in BaseFragment
 */

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val deleteNewsUseCase: DeleteNewsUseCase,
    private val getNewsUseCase: GetNewsUseCase,
    private val navManager: NavManager
) : BaseViewModel() {

    /**
     * Ui State
     * with typealias: LiveResult<T> = LiveData<WorldNewsResult<T>>
     */
    private val _stateScreen = MutableLiveResult<CountryViewState>(PendingResult())
    val stateScreen: LiveResult<CountryViewState> = _stateScreen

    /**
     * State : was update manual?
     */
    var isManualUpdate = false

    /**
     * Current country viewModel working with
     */
    private lateinit var currentCountry: Country

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
     * Loading news
     */
    fun loadData(currentCountry: Country, isForced: Boolean = false) {
        setCurrentCountry(currentCountry)
        getNewsList(isForced)
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
    private fun getNewsList(isForced: Boolean) = viewModelScope.launch {
        getNewsUseCase.execute(currentCountry, isForced).also { result ->
            when (result) {
                is GetNewsUseCase.UseCaseResult.Success -> {
                    _stateScreen.postValue(
                        SuccessResult(CountryViewState(news = result.data))
                    )
                }
                is GetNewsUseCase.UseCaseResult.Error -> {
                    _stateScreen.postValue(
                        ErrorResult(result.e.message.toString())
                    )
                }
                is GetNewsUseCase.UseCaseResult.Empty -> {
                    _stateScreen.postValue(
                        SuccessResult(CountryViewState(isEmpty = true))
                    )
                }
            }
        }
    }

    /**
     * Delete all news by country
     */
    fun deleteNewsByCountry(country: Country) = viewModelScope.launch {
        deleteNewsUseCase.execute(country)
        navManager.navigate(DeleteFinishedDialogDirections.actionGlobalNewsDeleted())
    }
}

/**
 * Inside Ui State of Country Fragment
 */
data class CountryViewState(
    val message: String = "",
    val isEmpty: Boolean = false,
    val news: List<Article> = listOf()
)