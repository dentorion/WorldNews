package com.entin.worldnews.presentation.ui.favourite

import androidx.lifecycle.viewModelScope
import com.entin.worldnews.domain.model.*
import com.entin.worldnews.domain.usecase.ChangeFavouriteUseCase
import com.entin.worldnews.domain.usecase.GetFavouriteNewsUseCase
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.base.viewmodel.LiveResult
import com.entin.worldnews.presentation.base.viewmodel.MutableLiveResult
import com.entin.worldnews.presentation.navigation.NavManager
import com.entin.worldnews.presentation.ui.country.components.ExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Favourite ViewModel
 * parent - [BaseViewModel] uses in BaseFragment
 */

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getFavouriteNewsUseCase: GetFavouriteNewsUseCase,
    private val changeFavouriteUseCase: ChangeFavouriteUseCase,
    private val navManager: NavManager
) : BaseViewModel() {

    /**
     * Ui State
     * with typealias: LiveResult<T> = LiveData<WorldNewsResult<T>>
     */
    private val _stateScreen = MutableLiveResult<ViewStateFavourites>(PendingResult())
    val uiStateFavourites: LiveResult<ViewStateFavourites> = _stateScreen

    private var currentList = listOf<Article>()

    override fun onRepeat() {
        getFavouriteNewsList()
    }

    /**
     * Deleting item news from favourite list
     */
    fun deleteFromFavouriteNews(url: String) = viewModelScope.launch {
        changeFavouriteUseCase(url)
        _stateScreen.postValue(
            SuccessResult(
                ViewStateFavourites(
                    news = currentList,
                    deleted = true
                )
            )
        )
    }

    /**
     * Navigation
     */
    fun navigateToArticleDetails(article: Article) {
        val navDirections =
            FavouriteNewsFragmentDirections.actionGlobalDetailFragment(article)
        navManager.navigate(navDirections)
    }

    /**
     * Get favourite news and react by quantity items
     */
    fun getFavouriteNewsList() = viewModelScope.launch {
        getFavouriteNewsUseCase().collect { useCaseResponse ->
            when (useCaseResponse) {
                UseCaseResult.Empty -> {
                    _stateScreen.postValue(
                        SuccessResult(
                            ViewStateFavourites(empty = true)
                        )
                    )
                }
                is UseCaseResult.Error -> {
                    _stateScreen.postValue(
                        ErrorResult(
                            ViewStateFavourites(
                                exception = useCaseResponse.e,
                                exceptionMessage = ExceptionMessage.NoInternet
                            )
                        )
                    )
                }
                is UseCaseResult.Success -> {
                    currentList = useCaseResponse.data
                    _stateScreen.postValue(
                        SuccessResult(
                            ViewStateFavourites(news = useCaseResponse.data)
                        )
                    )
                }
            }
        }
    }
}