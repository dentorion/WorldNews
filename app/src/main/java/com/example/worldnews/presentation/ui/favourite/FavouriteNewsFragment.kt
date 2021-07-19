package com.example.worldnews.presentation.ui.favourite

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.worldnews.R
import com.example.worldnews.databinding.FragmentFavouriteNewsBinding
import com.example.worldnews.presentation.extension.observe
import com.example.worldnews.presentation.extension.visible
import com.example.worldnews.presentation.ui.favourite.adapter.FavouriteNewsAdapter
import com.example.worldnews.presentation.ui.favourite.viewmodel.FavouriteViewModel
import com.example.worldnews.presentation.util.simpleShortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteNewsFragment :
    Fragment(R.layout.fragment_favourite_news) {

    private val binding: FragmentFavouriteNewsBinding by viewBinding()
    private val favouriteViewModel: FavouriteViewModel by viewModels()
    private val favouriteNewsAdapter: FavouriteNewsAdapter = FavouriteNewsAdapter()

    private val stateObserver = Observer<FavouriteViewModel.ViewState> { viewState ->
        with(binding) {
            if (!viewState.empty && !viewState.deleted) {
                favouriteNewsAdapter.submitList(viewState.news)
            }
            if (viewState.deleted) {
                simpleShortSnackBar(requireView(), "Not more favourite")
            }
            if (viewState.empty) {
                simpleShortSnackBar(requireView(), "Empty list")
                setIconTrash(viewState.empty)
            }
            progressBarFavouriteNews.visible = viewState.isLoading
            swipeLabel.visible = !viewState.empty
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDebouncedClickListener()

        initRecyclerView()

        initObserver()

        initDeleteSwipeGuest()

        loadData()
    }

    private fun initDebouncedClickListener() {
        favouriteNewsAdapter.setClickListener { article ->
            favouriteViewModel.navigateToArticleDetails(article)
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            listView.apply {
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

    private fun initObserver() {
        observe(favouriteViewModel.stateLiveData, stateObserver)
    }

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
                favouriteViewModel.deleteFromFavouriteNews(article.url)
            }
        }).attachToRecyclerView(binding.listView)
    }

    private fun loadData() {
        favouriteViewModel.loadData()
    }

    //

    private fun setIconTrash(isSeen: Boolean) {
        binding.iconTrash.visible = isSeen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listView.adapter = null
    }
}