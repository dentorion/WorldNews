package com.entin.worldnews.presentation.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entin.worldnews.R
import com.entin.worldnews.databinding.FragmentSearchBinding
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.base.fragment.extension.renderStateExtension
import com.entin.worldnews.presentation.extension.observe
import com.entin.worldnews.presentation.extension.textChanges
import com.entin.worldnews.presentation.util.alert.simpleShortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding()
    override val viewModel: SearchViewModel by viewModels()
    private val searchAdapter: SearchNewsAdapter = SearchNewsAdapter()
    private var searchJob: Job? = null

    /**
     * State observer
     */
    private val uiStateObserver = Observer<ViewModelResult<ViewStateSearch>> { uiState ->
        setState(uiState)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        // Init RecyclerView news
        initRecyclerView()

        // Setup Debounced Clicklistener for Adapter
        initRecyclerItemClickListener()

        // News searched data
        initUiStateObserver()

        // Repeat button
        initRepeatButton()

        // Listener of user typing
        initTextSearchListener()
    }

    /**
     * Each WorldNewsResult<ViewStateSearch> goes to extension function
     * and then to the BaseFragment, where it is render all views
     */
    private fun setState(uiState: ViewModelResult<ViewStateSearch>) {
        renderStateExtension(
            root = binding.root,
            uiState = uiState,
            onSuccess = { viewState ->
                onSuccess(viewState)
            },
        )
    }

    /**
     * What should be done onSuccess received
     */
    private fun onSuccess(viewState: ViewStateSearch) {
        if (viewState.news.isNotEmpty()) {
            searchAdapter.submitList(viewState.news)
        }
        if (viewState.empty) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.search_no_results)
            )
        }
        if (viewState.error) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.search_error_on_loading)
            )
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
                adapter = searchAdapter.apply {
                    setHasFixedSize(true)
                }
            }
        }
    }

    /**
     * Set click listener for Recycler elements
     */
    private fun initRecyclerItemClickListener() {
        searchAdapter.setClickListener { article ->
            viewModel.navigateToArticleDetails(article)
        }
    }

    /**
     * Listen to Ui State value
     */
    private fun initUiStateObserver() {
        observe(viewModel.uiStateSearch, uiStateObserver)
    }

    /**
     * Repeat button in merged layout triggers viewModel data loading function
     */
    private fun initRepeatButton() {
        super.initRepeatButton(partLoadingBinding = PartLoadingBinding.bind(binding.root))
    }

    /**
     * Text field to search news
     * Listen for changing and send search request every 750 mls
     */
    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initTextSearchListener() {
        with(binding) {
            searchJob?.cancel()
            searchJob = searchField.textChanges()
                .filterNot { it.isNullOrEmpty() }
                .debounce(750)
                .onEach {
                    viewModel.search(it.toString())
                }
                .launchIn(lifecycleScope)
        }
    }

    /**
     * Menu bar
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favourite -> {
                viewModel.navigateToFavourite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchAdapter.submitList(listOf())
        binding.newsRecyclerView.adapter = null
        searchJob?.cancel()
    }
}