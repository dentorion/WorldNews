package com.example.worldnews.presentation.ui.country.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.worldnews.NavgraphDirections
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.domain.usecase.DeleteNewsUseCase
import com.example.worldnews.domain.usecase.GetNewsUseCase
import com.example.worldnews.presentation.base.viewmodel.BaseAction
import com.example.worldnews.presentation.base.viewmodel.BaseViewModel
import com.example.worldnews.presentation.base.viewmodel.BaseViewState
import com.example.worldnews.presentation.navigation.NavManager
import com.example.worldnews.presentation.ui.dialogs.delete.DeleteFinishedDialogDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val deleteNewsUseCase: DeleteNewsUseCase,
    private val getNewsUseCase: GetNewsUseCase,
    private val navManager: NavManager
) : BaseViewModel<NewsViewModel.ViewState, NewsViewModel.Action>() {

    var isManualUpdate = false
    private lateinit var currentCountry: Country

    override fun onLoadData(country: Country?, isForced: Boolean) {
        country?.let {
            setCurrentCountry(country)
            getNewsList(isForced)
        }
    }

    private fun setCurrentCountry(country: Country) {
        currentCountry = country
    }

    private fun getNewsList(isForced: Boolean) {
        viewModelScope.launch {
            getNewsUseCase.execute(currentCountry, isForced).also { result ->
                val action = when (result) {
                    is GetNewsUseCase.UseCaseResult.Success -> {
                        Action.Success(result.data)
                    }

                    is GetNewsUseCase.UseCaseResult.Error -> {
                        Action.Failure
                    }

                    is GetNewsUseCase.UseCaseResult.Empty -> {
                        Action.Empty
                    }
                }
                sendAction(action)
            }
        }
    }

    override var state: ViewState by Delegates.observable(ViewState()) { _, old, new ->
        stateMutableLiveData.value = new
        if (isManualUpdate) {
            if (new == old) {
                stateMutableLiveData.value = ViewState(
                    isLoading = false,
                    isError = true,
                    errorText = "No updates"
                )
                isManualUpdate = false
            }
        }
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.Success -> ViewState(
            isLoading = false,
            news = viewAction.articles
        )
        is Action.Failure -> ViewState(
            isLoading = false,
            isError = true,
            errorText = "Loading error"
        )
        is Action.Empty -> ViewState(
            isLoading = true,
            isError = true,
            errorText = "Empty list"
        )
    }

    /**
     * Dialog delete old news
     * - clear news
     * - navigate to result screen
     */

    fun deleteNewsByCountry(country: Country) = viewModelScope.launch {
        deleteNewsUseCase.execute(country)
        navManager.navigate(DeleteFinishedDialogDirections.actionGlobalNewsDeleted())
    }

    // Navigation

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

    //

    data class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val errorText: String = "",
        val news: List<Article> = listOf()
    ) : BaseViewState

    sealed class Action : BaseAction {
        class Success(val articles: List<Article>) : Action()
        object Failure : Action()
        object Empty : Action()
    }
}
