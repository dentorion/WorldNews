package com.example.worldnews.presentation.ui.favourite.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.domain.usecase.GetFavouriteNewsUseCase
import com.example.worldnews.presentation.base.viewmodel.BaseAction
import com.example.worldnews.presentation.base.viewmodel.BaseViewModel
import com.example.worldnews.presentation.base.viewmodel.BaseViewState
import com.example.worldnews.presentation.navigation.NavManager
import com.example.worldnews.presentation.ui.favourite.FavouriteNewsFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getFavouriteNewsUseCase: GetFavouriteNewsUseCase,
    private val navManager: NavManager
) : BaseViewModel<FavouriteViewModel.ViewState, FavouriteViewModel.Action>() {

    override fun onLoadData(country: Country?, isForced: Boolean) {
        getFavouriteNewsList()
    }

    override var state: ViewState by Delegates.observable(ViewState()) { _, _, new ->
        stateMutableLiveData.value = new
    }

    private fun getFavouriteNewsList() {
        viewModelScope.launch {
            getFavouriteNewsUseCase.execute().collect { news ->
                sendAction(Action.Success(news))
                if (news.isEmpty()) {
                    sendAction(Action.Empty)
                }
            }
        }
    }

    fun navigateToArticleDetails(article: Article) {
        val navDirections =
            FavouriteNewsFragmentDirections.actionGlobalDetailFragment(article)
        navManager.navigate(navDirections)
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.Success -> ViewState(
            isLoading = false,
            news = viewAction.articles,
            empty = false,
            deleted = false
        )
        is Action.Empty -> ViewState(
            isLoading = false,
            news = listOf(),
            empty = true,
            deleted = false
        )
        is Action.Deleted -> ViewState(
            isLoading = false,
            news = listOf(),
            empty = false,
            deleted = true
        )
    }

    fun deleteFromFavouriteNews(url: String) = viewModelScope.launch {
        getFavouriteNewsUseCase.deleteFromFavouriteNews(url)
        sendAction(Action.Deleted)
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val news: List<Article> = listOf(),
        val empty: Boolean = true,
        val deleted: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        class Success(val articles: List<Article>) : Action()
        object Empty : Action()
        object Deleted : Action()
    }

}