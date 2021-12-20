package com.entin.worldnews.presentation.base.fragment

import androidx.fragment.app.Fragment
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.ErrorResult
import com.entin.worldnews.domain.model.PendingResult
import com.entin.worldnews.domain.model.SuccessResult
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.viewmodel.BaseViewModel
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
        uiState: ViewModelResult<T>,
        onPending: () -> Unit,
        onError: (T) -> Unit,
        onSuccess: (T) -> Unit,
    ) = when (uiState) {
        is ErrorResult -> onError(uiState.data)
        is PendingResult -> onPending()
        is SuccessResult -> onSuccess(uiState.data)
    }

    fun initRepeatButton(partLoadingBinding: PartLoadingBinding) {
        partLoadingBinding.tryAgainButtonPart.setOnClickListener {
            viewModel.onRepeat()
        }
    }
}