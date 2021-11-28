package com.entin.worldnews.presentation.ui.favourite

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entin.worldnews.R
import com.entin.worldnews.databinding.FragmentFavouriteNewsBinding
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.base.fragment.extension.renderStateExtension
import com.entin.worldnews.presentation.extension.observe
import com.entin.worldnews.presentation.extension.visible
import com.entin.worldnews.presentation.util.simpleShortSnackBar
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
            listViewFavourites.apply {
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
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, tg: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val article = favouriteNewsAdapter.currentList[viewHolder.absoluteAdapterPosition]
                viewModel.deleteFromFavouriteNews(article.url)
            }
        }).attachToRecyclerView(binding.listViewFavourites)
    }

    /**
     * What should be done onSuccess received
     */
    private fun onSuccess(viewState: ViewStateFavourites) {
        if (!viewState.empty && !viewState.deleted) {
            favouriteNewsAdapter.submitList(viewState.news)
        }
        if (viewState.deleted) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.favourite_not_in_list_no_more)
            )
        }
        if (viewState.empty) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.favourite_empty_list)
            )
        }
        setSwipeLabel(viewState.empty)
        setIconTrash(viewState.empty)
    }

    /**
     * Icon basket with trash
     */
    private fun setIconTrash(isSeen: Boolean) {
        binding.iconTrash.visible = isSeen
    }

    /**
     * Label about swiping
     */
    private fun setSwipeLabel(isSeen: Boolean) {
        binding.swipeLabel.visible = !isSeen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listViewFavourites.adapter = null
    }
}