package com.entin.worldnews.presentation.base.fragment

import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.entin.worldnews.R
import com.entin.worldnews.domain.model.ErrorResult
import com.entin.worldnews.domain.model.PendingResult
import com.entin.worldnews.domain.model.SuccessResult
import com.entin.worldnews.domain.model.WorldNewsResult
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
import com.entin.worldnews.presentation.extension.visible
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base Fragment
 */

@AndroidEntryPoint
abstract class BaseFragment(layoutRes: Int) : Fragment(layoutRes) {

    /**
     * ViewModel for Fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Render views in Fragment with gotten lambdas
     */
    fun <T> renderResult(
        root: ViewGroup,
        UiState: WorldNewsResult<T>,
        onPending: () -> Unit,
        onError: (String) -> Unit,
        onSuccess: (T) -> Unit,
    ) {
        root.children
                // Country Fragment
            .filter { it.id != R.id.no_internet_part }
                // Search Fragment
            .filter { it.id != R.id.search_layout && it.id != R.id.searchField }
                // Set all views invisible
            .forEach { it.visible = false }

        when (UiState) {
            is ErrorResult -> onError(UiState.exception)
            is PendingResult -> onPending()
            is SuccessResult -> onSuccess(UiState.data)
        }
    }
}