package com.entin.worldnews.presentation.ui.favourite

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entin.worldnews.R
import com.entin.worldnews.databinding.FragmentFavouriteNewsBinding
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.base.fragment.extension.renderStateExtension
import com.entin.worldnews.presentation.extension.observe
import com.entin.worldnews.presentation.extension.visible
import com.entin.worldnews.presentation.util.alert.simpleLongSnackBar
import com.entin.worldnews.presentation.util.alert.simpleShortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteNewsFragment :
    BaseFragment(R.layout.fragment_favourite_news) {

    private val binding: FragmentFavouriteNewsBinding by viewBinding()
    override val viewModel: FavouriteViewModel by viewModels()
    private val favouriteNewsAdapter: FavouriteNewsAdapter = FavouriteNewsAdapter()

    /**
     * State observer
     */
    private val uiStateObserver = Observer<ViewModelResult<ViewStateFavourites>> { uiState ->
        setState(uiState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerItemClickListener()

        initRecyclerView()

        initUiStateObserver()

        initRepeatButton()

        initDeleteSwipeGuest()

        loadData()
    }

    /**
     * Request for favourite news
     */
    private fun loadData() {
        viewModel.getFavouriteNewsList()
    }

    /**
     * Set click listener for Recycler elements
     */
    private fun initRecyclerItemClickListener() {
        favouriteNewsAdapter.setClickListener { article ->
            viewModel.navigateToArticleDetails(article)
        }
    }

    /**
     * RecyclerView with Adapter
     */
    private fun initRecyclerView() {
        with(binding) {
            newsRecyclerView.apply {
                layoutManager = when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        LinearLayoutManager(
                            requireContext().applicationContext,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    }
                    else -> {
                        LinearLayoutManager(
                            requireContext().applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                }
                adapter = favouriteNewsAdapter.apply {
                    setHasFixedSize(true)
                }
            }
        }
    }

    /**
     * Listen to Ui State value
     */
    private fun initUiStateObserver() {
        observe(viewModel.uiStateFavourites, uiStateObserver)
    }

    /**
     * Repeat button in merged layout triggers viewModel data loading function
     */
    private fun initRepeatButton() {
        super.initRepeatButton(partLoadingBinding = PartLoadingBinding.bind(binding.root))
    }

    /**
     * Each WorldNewsResult<ViewStateFavourites> goes to extension function
     * and then to the BaseFragment, where it is render all views
     */
    private fun setState(uiState: ViewModelResult<ViewStateFavourites>) {
        renderStateExtension(
            root = binding.root,
            uiState = uiState,
            onSuccess = { viewState ->
                onSuccess(viewState)
            },
        )
    }

    /**
     * Swipe guest to delete from favourite news
     */
    private fun initDeleteSwipeGuest() {
        ItemTouchHelper(
            ItemTouchHelperCallback(favouriteNewsAdapter, viewModel)
        ).attachToRecyclerView(binding.newsRecyclerView)
    }

    /**
     * What should be done onSuccess received
     */
    private fun onSuccess(viewState: ViewStateFavourites) {
        // Set list of news
        favouriteNewsAdapter.submitList(viewState.news)
        // Show message after news delete
        if (viewState.deleted) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.favourite_not_in_list_no_more)
            )
        }
        // Show message on empty list of news
        if (viewState.empty) {
            simpleLongSnackBar(
                requireView(),
                requireContext().getString(R.string.favourite_empty_list)
            )
        }
        // Show labels about swiping
        setSwipeLabel(viewState.empty)
        setIconTrash(viewState.empty)
    }

    /**
     * Icon basket with trash
     */
    private fun setIconTrash(isSeen: Boolean) {
        Log.i("Eska", "setIconTrash() : $isSeen")
        binding.iconTrash.visible = isSeen
    }

    /**
     * Label about swiping
     */
    private fun setSwipeLabel(isSeen: Boolean) {
        Log.i("Eska", "setSwipeLabel() : $isSeen")
        binding.swipeLabel.visible = !isSeen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.newsRecyclerView.adapter = null
    }
}