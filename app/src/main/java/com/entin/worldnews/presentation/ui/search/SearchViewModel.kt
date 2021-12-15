package com.entin.worldnews.presentation.ui.search

import androidx.lifecycle.viewModelScope
import com.entin.worldnews.NavgraphDirections
import com.entin.worldnews.domain.model.*
import com.entin.worldnews.domain.usecase.SaveSearchedAndOpenedArticleUseCase
import com.entin.worldnews.domain.usecase.SearchNewsByQueryUseCase
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.base.viewmodel.LiveResult
import com.entin.worldnews.presentation.base.viewmodel.MutableLiveResult
import com.entin.worldnews.presentation.navigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsByQueryUseCase: SearchNewsByQueryUseCase,
    private val saveSearchedAndOpenedArticleUseCase: SaveSearchedAndOpenedArticleUseCase,
    private val navManager: NavManager
) : BaseViewModel() {

    /**
     * Ui State
     * with typealias: LiveResult<T> = LiveData<WorldNewsResult<T>>
     */
    private val _stateScreen = MutableLiveResult(SuccessResult(ViewStateSearch()))
    val uiStateSearch: LiveResult<ViewStateSearch> = _stateScreen

    /**
     * Job of getting news
     */
    private var myJob: Job? = null

    /**
     * Last input query search
     */
    private var lastQuery = ""

    override fun onRepeat() {
        search(lastQuery)
    }

    /**
     * Search news function
     */
    fun search(searchInput: String) {
        lastQuery = searchInput
        myJob?.cancel()
        myJob = viewModelScope.launch {
            _stateScreen.postValue(PendingResult())

            searchNewsByQueryUseCase(search = searchInput).collect { result ->
                when (result) {
                    UseCaseResult.Empty -> {
                        _stateScreen.postValue(SuccessResult(ViewStateSearch(empty = true)))
                    }
                    is UseCaseResult.Error -> {
                        _stateScreen.postValue(ErrorResult(result.e.message!!))
                    }
                    is UseCaseResult.Success -> {
                        _stateScreen.postValue(SuccessResult(ViewStateSearch(result = result.data)))
                    }
                }
            }
        }
    }

    /**
     * Navigation
     */
    fun navigateToArticleDetails(article: Article) {
        saveArticleFromSearch(article)
        val navDirections = NavgraphDirections.actionGlobalDetailFragment(article)
        navManager.navigate(navDirections)
    }

    fun navigateToFavourite() {
        val navDirections = NavgraphDirections.actionGlobalFavouriteNewsFragment()
        navManager.navigate(navDirections)
    }

    /**
     * Save article from search result that was opened
     */
    private fun saveArticleFromSearch(article: Article) = viewModelScope.launch {
        saveSearchedAndOpenedArticleUseCase(article)
    }
}

/**
 * Inside Ui State of Search Fragment
 */
data class ViewStateSearch(
    val initial: Boolean = true,
    val error: Boolean = false,
    val empty: Boolean = false,
    val result: List<Article> = listOf()
)