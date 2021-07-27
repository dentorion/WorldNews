package com.example.worldnews.presentation.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.worldnews.R
import com.example.worldnews.databinding.FragmentSearchBinding
import com.example.worldnews.presentation.extension.visible
import com.example.worldnews.presentation.ui.country.adapter.SearchNewsAdapter
import com.example.worldnews.presentation.ui.search.viewmodel.SearchViewModel
import com.example.worldnews.presentation.util.simpleShortSnackBar
import com.example.worldnews.presentation.extension.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding()
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter: SearchNewsAdapter = SearchNewsAdapter()
    private var searchJob: Job? = null

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        // Init RecyclerView news
        initRecycler()

        // Setup Debounced Clicklistener for Adapter
        initDebouncedClickListener()

        // News searched data
        initObserver()

        // Listener of user typing
        initTextSearchListener()
    }

    private fun initRecycler() {
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
                adapter = searchAdapter.apply {
                    setHasFixedSize(true)
                }
            }
        }
    }

    private fun initDebouncedClickListener() {
        searchAdapter.setClickListener { article ->
            searchViewModel.navigateToArticleDetails(article)
        }
    }

    private fun initObserver() {
        searchViewModel.stateEvent
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { event ->
                showWaitIcon(false)
                when (event) {
                    is SearchViewModel.SearchEvents.Success -> {
                        val articles = event.articles
                        if (articles.isNotEmpty()) {
                            searchAdapter.submitList(articles)
                        }
                    }
                    is SearchViewModel.SearchEvents.Empty -> {
                        simpleShortSnackBar(requireView(), requireContext().getString(R.string.search_no_results))
                    }
                    is SearchViewModel.SearchEvents.Error -> {
                        simpleShortSnackBar(requireView(), requireContext().getString(R.string.search_error_on_loading))
                    }
                }
            }.launchIn(lifecycleScope)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initTextSearchListener() {
        with(binding) {
            searchJob?.cancel()
            searchJob = search.textChanges()
                .filterNot { it.isNullOrEmpty() }
                .debounce(750)
                .onEach {
                    searchViewModel.search(it.toString())
                    showWaitIcon(true)
                }
                .launchIn(lifecycleScope)
        }
    }

    // Icon for loading process

    private fun showWaitIcon(mode: Boolean) {
        binding.progressBar.visible = mode
    }

    // Menu bar

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favourite -> {
                searchViewModel.navigateToFavourite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Destroy

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listView.adapter = null
        searchJob?.cancel()
    }

}