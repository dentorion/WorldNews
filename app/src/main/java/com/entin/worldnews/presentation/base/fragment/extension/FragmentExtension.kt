package com.entin.worldnews.presentation.base.fragment.extension

import android.view.ViewGroup
import com.entin.worldnews.R
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.extension.visible
import com.entin.worldnews.presentation.util.simpleShortSnackBar

/**
 * Takes :
 *  - [root] (binding.root) for getting inside elements
 *  - [uiState] for correct reacting on viewModel response
 *  - [onSuccess] for Success response reaction
 *
 *  Invokes :
 *  - BaseFragment function that hide all elements of root
 *    and switch on only necessary from onError / onPending response
 */

fun <T> BaseFragment.renderStateExtension(
    root: ViewGroup,
    uiState: ViewModelResult<T>,
    onSuccess: (T) -> Unit,
) {
    val binding = PartLoadingBinding.bind(root)

    renderResult(
        uiState = uiState,
        onSuccess = { data ->
            binding.errorMessagePart.visible = false
            binding.tryAgainButtonPart.visible = false
            binding.progressBarPart.visible = false

            onSuccess(data)
        },
        onError = { errorString ->
            binding.errorMessagePart.visible = true
            binding.tryAgainButtonPart.visible = true

            simpleShortSnackBar(
                requireView(),
                String.format(requireContext().getString(R.string.alert_error_news), errorString)
            )
        },
        onPending = {
            binding.progressBarPart.visible = true
        },
    )
}