package com.entin.worldnews.presentation.ui.search

import androidx.lifecycle.viewModelScope
import com.entin.worldnews.NavgraphDirections
import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.PendingResult
import com.entin.worldnews.domain.model.SuccessResult
import com.entin.worldnews.domain.usecase.SearchNewsUseCase
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.base.viewmodel.LiveResult
import com.entin.worldnews.presentation.base.viewmodel.MutableLiveResult
import com.entin.worldnews.presentation.navigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val navManager: NavManager
) : BaseViewModel() {

    /**
     * Ui State
     * with typealias: LiveResult<T> = LiveData<WorldNewsResult<T>>
     */
    private val _stateScreen = MutableLiveResult(SuccessResult(ViewStateSearch()))
    val uiStateSearch: LiveResult<ViewStateSearch> = _stateScreen

    fun search(searchInput: String) = viewModelScope.launch {
        _stateScreen.postValue(PendingResult())

        searchNewsUseCase.execute(search = searchInput).also { result ->
            when (result) {
                SearchNewsUseCase.UseCaseResult.Empty -> {
                    _stateScreen.postValue(SuccessResult(ViewStateSearch(empty = true)))
                }
                is SearchNewsUseCase.UseCaseResult.Error -> {
                    _stateScreen.postValue(SuccessResult(ViewStateSearch(error = true)))
                }
                is SearchNewsUseCase.UseCaseResult.Success -> {
                    _stateScreen.postValue(SuccessResult(ViewStateSearch(result = result.data)))
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
        searchNewsUseCase.saveSearchedAndOpenedArticle(article)
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