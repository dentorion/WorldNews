package com.example.worldnews.presentation.ui.country

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.worldnews.R
import com.example.worldnews.databinding.FragmentCountryNewsBinding
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.presentation.extension.observe
import com.example.worldnews.presentation.extension.visible
import com.example.worldnews.presentation.ui.country.adapter.CountryNewsAdapter
import com.example.worldnews.presentation.ui.country.viewmodel.NewsViewModel
import com.example.worldnews.presentation.ui.dialogs.delete.DeleteFinishedDialog
import com.example.worldnews.presentation.util.ConnectionLiveData
import com.example.worldnews.presentation.util.simpleShortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment(private val currentCountry: Country) :
    Fragment(R.layout.fragment_country_news) {

    private val binding: FragmentCountryNewsBinding by viewBinding()
    private val newsViewModel: NewsViewModel by viewModels()
    private val newsAdapter: CountryNewsAdapter = CountryNewsAdapter()

    /**
     * Check connection to Internet
     */
    @Inject
    lateinit var connectionManager: ConnectionLiveData
    private var isConnected = true
    private val connectObs = Observer<Boolean> { connection ->
        isConnected = connection
        showConnectionFailed(connection)
    }

    /**
     * State observer
     */
    private val stateObserver = Observer<NewsViewModel.ViewState> { viewState ->

        populateNews(viewState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        // Internet + News data
        initObservers()

        // Setup Debounced Clicklistener for Adapter
        initDebouncedClickListener()

        // Init RecyclerView news
        initRecyclerView()

        // Swiping is refreshing
        initSwipeGuest()

        // Request News from ViewModel
        loadData()
    }

    private fun initObservers() {
        // Internet connection
        observe(connectionManager, connectObs)

        // News observer
        observe(newsViewModel.stateLiveData, stateObserver)
    }

    private fun initDebouncedClickListener() {
        newsAdapter.setOnDebouncedClickListener { article ->
            newsViewModel.navigateToArticleDetails(article)
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
                adapter = newsAdapter.apply {
                    setHasFixedSize(true)
                }
            }
        }
    }

    private fun initSwipeGuest() {
        with(binding) {
            swipeRefresh.apply {
                setColorSchemeResources(R.color.colorAccent)
                setOnRefreshListener {
                    loadData()
                    newsViewModel.isManualUpdate = true
                    isRefreshing = false
                }
            }
        }
    }

    private fun loadData(isForced: Boolean = false) {
        if (!isConnected) {
            simpleShortSnackBar(requireView(), "Available only saved news")
        }
        newsViewModel.loadData(currentCountry, isForced)
    }

    // Observers behaviour

    private fun showConnectionFailed(isConnected: Boolean) {
        if (isConnected) {
            binding.noInternet.visibility = View.GONE
        } else {
            binding.noInternet.visible = true
        }
    }

    private fun populateNews(viewState: NewsViewModel.ViewState) {
        binding.progressBar.visible = viewState.isLoading
        if (viewState.isError) {
            simpleShortSnackBar(requireView(), viewState.errorText)
        }
        if (viewState.news.isNotEmpty()) {
            newsAdapter.submitList(viewState.news)
        }
    }

    // Menu bar

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_country, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                newsViewModel.navigateToSearch()
                true
            }
            R.id.action_favourite -> {
                newsViewModel.navigateToFavourite()
                true
            }
            R.id.action_forced_download -> {
                loadData(true)
                true
            }
            R.id.action_delete_all_tasks -> {
                DeleteFinishedDialog(currentCountry).show(childFragmentManager, "TAG")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Destroy

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listView.adapter = null
    }
}