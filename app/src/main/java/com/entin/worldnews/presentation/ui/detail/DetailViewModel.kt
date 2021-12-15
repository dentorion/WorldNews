package com.entin.worldnews.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.entin.worldnews.NavgraphDirections
import com.entin.worldnews.domain.model.Article
import com.entin.worldnews.domain.model.PendingResult
import com.entin.worldnews.domain.model.SuccessResult
import com.entin.worldnews.domain.usecase.IsArticleFavouriteUseCase
import com.entin.worldnews.domain.usecase.ChangeFavouriteUseCase
import com.entin.worldnews.domain.usecase.SetArticleWatchedUseCase
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.base.viewmodel.LiveResult
import com.entin.worldnews.presentation.base.viewmodel.MutableLiveResult
import com.entin.worldnews.presentation.navigation.NavManager
import com.entin.worldnews.presentation.util.date.MapperDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DetailViewModel
 * parent - [BaseViewModel] uses in BaseFragment
 */

@InternalCoroutinesApi
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val setArticleWatchedUseCase: SetArticleWatchedUseCase,
    private val changeFavouriteClickUseCase: ChangeFavouriteUseCase,
    private val isArticleFavouriteUseCase: IsArticleFavouriteUseCase,
    private val navManager: NavManager
) : BaseViewModel() {

    /**
     * Ui state fo Detail fragment
     * LiveData (LiveResult)
     */
    private val _uiStateDetailFragment = MutableLiveResult<DetailViewState>(PendingResult())
    val uiStateDetailFragment: LiveResult<DetailViewState> = _uiStateDetailFragment

    /**
     * Like icon state for Detail fragment
     * Channel -> Flow
     */
    private val _favouriteChannel = Channel<Boolean>()
    val favouriteChannel = _favouriteChannel.receiveAsFlow()

    override fun onRepeat() {
        getArticle()
    }

    /**
     * Create inside data for ui state with Article
     * Set Article as watched
     * Get favourite status of Article
     */
    fun getArticle() = viewModelScope.launch {
        val article = state.get<Article>("article")
        // Date time is taken from SavedStateHandle or Fragment Argument or Empty string
        val dateTime =
            state.get<String>("publishedAt") ?: state.get<Article>("article")?.publishedAt ?: ""
        /**
         * Response with Success and inside Article data by LiveResult
         * SavedStateHandle -> Fragment argument -> Empty string
         */
        _uiStateDetailFragment.postValue(
            SuccessResult(
                DetailViewState(
                    title = state.get<String>("title") ?: article?.title ?: "",
                    author = state.get<String>("author") ?: article?.author ?: "",
                    description = state.get<String>("description") ?: article?.description ?: "",
                    url = state.get<String>("url") ?: article?.url ?: "",
                    urlToImage = state.get<String>("urlToImage") ?: article?.urlToImage ?: "",
                    publishedAt = MapperDate.cropPublishedAtToDate(dateTime),
                    publishedTime = MapperDate.cropPublishedAtToTime(dateTime),
                )
            )
        )

        // Set Article as watched
        setArticleWatchedUseCase.invoke(url = state.get<String>("url") ?: article?.url ?: "")

        // Get favourite status of Article
        isArticleFavouriteUseCase(url = state.get<String>("url") ?: article?.url ?: "")
            .flowOn(Dispatchers.IO).collect {
                _favouriteChannel.send(it)
            }
    }

    /**
     * Favourite icon button was clicked by user
     */
    fun favouriteBtnClick(url: String) = viewModelScope.launch {
        changeFavouriteClickUseCase(url = url)
    }

    /**
     * Navigation
     */
    fun navigateToSearch() {
        val navDirections = NavgraphDirections.actionGlobalSearchFragment()
        navManager.navigate(navDirections)
    }

    fun navigateToFavourite() {
        navManager.navigate(DetailFragmentDirections.actionGlobalFavouriteNewsFragment())
    }
}

/**
 * Inside Ui State of Detail Fragment
 */
data class DetailViewState(
    val title: String,
    val author: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val publishedTime: String
)