package com.entin.worldnews.presentation.base.fragment.extension

import android.view.ViewGroup
import androidx.core.view.children
import com.entin.worldnews.R
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.WorldNewsResult
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
    uiState: WorldNewsResult<T>,
    onSuccess: (T) -> Unit,
) {
    val binding = PartLoadingBinding.bind(root)

    renderResult(
        root = root,
        UiState = uiState,
        onSuccess = { data ->
            root.children
                // Special merged layout
                .filter {
                    it.id != R.id.progressBarPart && it.id != R.id.try_again_button_part &&
                            it.id != R.id.error_message_part
                }

                // Country Fragment
                .filter { it.id != R.id.no_internet_part }

                // Search Fragment
                .filter { it.id != R.id.search_layout && it.id != R.id.searchField }

                // Make all views visible
                .forEach { it.visible = true }

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