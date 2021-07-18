package com.example.worldnews.presentation.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldnews.NavgraphDirections
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.domain.usecase.SearchNewsUseCase
import com.example.worldnews.presentation.navigation.NavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val navManager: NavManager
) : ViewModel() {

    private val _stateEvent = Channel<SearchEvents>()
    val stateEvent = _stateEvent.receiveAsFlow()

    fun search(searchInput: String) = viewModelScope.launch {
        searchNewsUseCase.execute(search = searchInput).also { result ->
            when (result) {
                SearchNewsUseCase.UseCaseResult.Empty -> {
                    _stateEvent.send(SearchEvents.Empty)
                }
                is SearchNewsUseCase.UseCaseResult.Error -> {
                    _stateEvent.send(SearchEvents.Error)
                }
                is SearchNewsUseCase.UseCaseResult.Success -> {
                    _stateEvent.send(SearchEvents.Success(result.data))
                }
            }
        }
    }

    // Navigation

    fun navigateToArticleDetails(article: Article) {
        saveArticle(article)
        val navDirections = NavgraphDirections.actionGlobalDetailFragment(article)
        navManager.navigate(navDirections)
    }

    // Save article from search list that was opened

    private fun saveArticle(article: Article) = viewModelScope.launch {
        searchNewsUseCase.saveSearchedAndOpenedArticle(article)
    }

    sealed class SearchEvents {
        object Error : SearchEvents()
        object Empty : SearchEvents()
        data class Success(val articles: List<Article>) : SearchEvents()
    }
}
