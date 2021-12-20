package com.entin.worldnews.presentation.base.fragment.extension

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.entin.worldnews.R
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.extension.visible
import com.entin.worldnews.presentation.ui.country.components.ExceptionMessage
import com.entin.worldnews.presentation.util.ViewState
import com.entin.worldnews.presentation.util.alert.simpleShortSnackBar

/**
 * Takes :
 *  - [root] (binding.root) for getting inside elements of [PartLoadingBinding]
 *  - [uiState] for correct reacting on viewModel response
 *  - [onSuccess] for Success response reaction
 *
 *  Invokes :
 *  - BaseFragment function that hide all elements of root
 *    and switch on only necessary view elements from onError / onPending response
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
            errorElementVisibility(
                binding = binding,
                progressBar = false,
                errorMessage = false,
                tryAgainButtonPart = false,
            )
            recyclerViewVisibility(root, true)

            onSuccess(data)
        },
        onError = { data ->
            errorElementVisibility(
                binding = binding,
                progressBar = false,
                errorMessage = true,
                tryAgainButtonPart = true,
            )
            recyclerViewVisibility(root, false)

            alertErrorMessage(data)
        },
        onPending = {
            errorElementVisibility(
                binding = binding,
                progressBar = true,
                errorMessage = false,
                tryAgainButtonPart = false,
            )
            recyclerViewVisibility(root, false)
        },
    )
}

/**
 * SnackBar message
 */
private fun <T> BaseFragment.alertErrorMessage(data: T) {
    simpleShortSnackBar(
        requireView(),
        String.format(requireContext().getString(R.string.alert_error_news), getErrorString(data))
    )
}

/**
 * Returns string based on error type of [ExceptionMessage]
 */
private fun <T> BaseFragment.getErrorString(data: T) =
    when ((data as ViewState).exceptionMessage) {
        ExceptionMessage.NoInternet -> requireContext().getString(R.string.no_network_connection)
        else -> requireContext().getString(R.string.alert_unknown_error)
    }

/**
 * Error elements visibility
 */
private fun errorElementVisibility(
    binding: PartLoadingBinding,
    progressBar: Boolean,
    errorMessage: Boolean,
    tryAgainButtonPart: Boolean
) {
    binding.errorMessagePart.visible = errorMessage
    binding.tryAgainButtonPart.visible = tryAgainButtonPart
    binding.progressBarPart.visible = progressBar
}

/**
 * Only for fragments with RecyclerView of news: Country, Search
 */
private fun recyclerViewVisibility(root: ViewGroup, isVisible: Boolean) {
    root.findViewById<RecyclerView>(R.id.newsRecyclerView)?.isVisible = isVisible
}