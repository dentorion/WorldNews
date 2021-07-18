package com.example.worldnews.presentation.ui.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldnews.NavgraphDirections
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.domain.usecase.FavouriteIconUseCase
import com.example.worldnews.domain.usecase.SetArticleWatchedUseCase
import com.example.worldnews.presentation.navigation.NavManager
import com.example.worldnews.presentation.ui.detail.DetailFragmentDirections
import com.example.worldnews.presentation.util.MapperDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class DetailViewModel @Inject constructor(
    state: SavedStateHandle,
    private val setArticleWatchedUseCase: SetArticleWatchedUseCase,
    private val favouriteUseCase: FavouriteIconUseCase,
    private val navManager: NavManager
) : ViewModel() {

    private val _articleChannel = Channel<ViewState>()
    val articleChannel = _articleChannel.receiveAsFlow()

    private val _favouriteChannel = Channel<Boolean>()
    val favouriteChannel = _favouriteChannel.receiveAsFlow()

    val article = state.get<Article>("article")
    private val articleTitle = state.get<String>("title") ?: article?.title ?: ""
    private val articleAuthor = state.get<String>("author") ?: article?.author ?: ""
    private val articleDescription = state.get<String>("description") ?: article?.description ?: ""
    private val articleUrlToImage = state.get<String>("urlToImage") ?: article?.urlToImage ?: ""
    private val articleUrl = state.get<String>("url") ?: article?.url ?: ""

    private val dateTime = state.get<String>("publishedAt") ?: article?.publishedAt
    private var articlePublishedAt = ""
    private var articlePublishedTime = ""

    init {
        if (dateTime != null) {
            articlePublishedAt = MapperDate.cropPublishedAtToDate(dateTime)
            articlePublishedTime = MapperDate.cropPublishedAtToTime(dateTime)
        }
    }

    fun getArticle() = viewModelScope.launch {
        _articleChannel.send(
            ViewState(
                title = articleTitle,
                author = articleAuthor,
                description = articleDescription,
                url = articleUrl,
                urlToImage = articleUrlToImage,
                publishedAt = articlePublishedAt,
                publishedTime = articlePublishedTime
            )
        )

        setArticleWatchedUseCase.execute(url = articleUrl)

        favouriteUseCase.get(url = articleUrl).flowOn(Dispatchers.IO).collect {
            _favouriteChannel.send(it)
        }
    }

    fun favouriteBtnClick(url: String) = viewModelScope.launch {
        favouriteUseCase.set(url = url)
    }

    fun navigateToSearch() {
        val navDirections = NavgraphDirections.actionGlobalSearchFragment()
        navManager.navigate(navDirections)
    }

    fun navigateToFavourite() {
        navManager.navigate(DetailFragmentDirections.actionGlobalFavouriteNewsFragment())
    }
}