package com.entin.worldnews.presentation.ui.country.components

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.entin.worldnews.R
import com.entin.worldnews.databinding.FragmentCountryNewsBinding
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.model.WorldNewsResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.base.fragment.extension.renderStateExtension
import com.entin.worldnews.presentation.extension.observe
import com.entin.worldnews.presentation.extension.visible
import com.entin.worldnews.presentation.ui.dialogs.delete.DeleteFinishedDialog
import com.entin.worldnews.presentation.util.ConnectionLiveData
import com.entin.worldnews.presentation.util.simpleShortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Country fragment
 * Parent for Pl, Ua, Us, Ru fragments
 * Child for BaseFragment
 */

@AndroidEntryPoint
open class CountryFragment(
    private val currentCountry: Country
) : BaseFragment(R.layout.fragment_country_news) {

    // Binding main
    private var _binding: FragmentCountryNewsBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    override val viewModel: CountryViewModel by viewModels()

    // RecycleView Adapter - List Adapter
    private val newsAdapter = CountryNewsAdapter()

    /**
     * Check connection to Internet observer
     */
    @Inject
    lateinit var connectionManager: ConnectionLiveData
    private var isConnected = true

    /**
     * State observer
     */
    private val stateObserver = Observer<WorldNewsResult<CountryViewState>> { result ->
        setState(result)
    }

    /**
     * Internet connection observer and
     * Show alert about Internet connection lost
     */
    private val connectionObserver = Observer<Boolean> { connection ->
        isConnected = connection
        // true -> is connection / visible = false
        // false -> no connection / visible = true
        Log.i("WWW", "connection is $connection")
        binding.noInternetPart.visible = !isConnected
    }

    /**
     * View binding inflate with classical Google scheme
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // All functions here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initObservers()

        initRecyclerClickListener()

        initRecyclerLayoutManager()

        initSwipeGuest()

        loadData()
    }

    /**
     * Subscribe observers and react on changing
     *  - Internet connection
     *  - Ui state from viewModel
     */
    private fun initObservers() {
        // Internet connection
        observe(connectionManager, connectionObserver)

        // Ui state observer
        observe(viewModel.stateScreen, stateObserver)
    }

    /**
     * React on ViewHolder in RecyclerView click
     */
    private fun initRecyclerClickListener() {
        newsAdapter.setClickListener { article ->
            viewModel.navigateToArticleDetails(article)
        }
    }

    /**
     * RecyclerView Layout Manager depends on orientation
     */
    private fun initRecyclerLayoutManager() {
        val orientationLayoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                LinearLayoutManager.VERTICAL
            }
            else -> {
                LinearLayoutManager.HORIZONTAL
            }
        }

        with(binding) {
            listView.apply {
                layoutManager = LinearLayoutManager(
                    requireContext().applicationContext,
                    orientationLayoutManager,
                    false
                )
                adapter = newsAdapter.apply {
                    setHasFixedSize(true)
                }
            }
        }
    }

    /**
     * Swipe guest for refresh
     */
    private fun initSwipeGuest() {
        with(binding) {
            swipeRefresh.apply {
                setColorSchemeResources(R.color.colorAccent)
                setOnRefreshListener {
                    viewModel.isManualUpdate = true
                    isRefreshing = false
                    loadData()
                }
            }
        }
    }

    /**
     * News get from viewModel
     */
    private fun loadData(isForced: Boolean = false) {
        if (!isConnected) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.alert_internet_connection_error)
            )
        }
        viewModel.loadData(currentCountry, isForced)
    }

    /**
     * Each WorldNewsResult<CountryViewState> goes to extension function
     * and then to the BaseFragment, where it is render all views
     */
    private fun setState(uiState: WorldNewsResult<CountryViewState>) {
        renderStateExtension(
            root = binding.root,
            uiState = uiState,
            onSuccess = { viewState ->
                if (viewState.isEmpty) {
                    simpleShortSnackBar(
                        requireView(),
                        requireContext().getString(R.string.alert_empty_news)
                    )
                }
                if (viewState.news.isNotEmpty()) {
                    Log.i("WWW", "CountryFragment. setState. Size: ${viewState.news.size}")
                    newsAdapter.submitList(viewState.news)
                }
            },
        )
    }

    /**
     * Menu bar
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_country, menu)
    }

    /**
     * Country bottom menu listener
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                viewModel.navigateToSearch()
                true
            }
            R.id.action_favourite -> {
                viewModel.navigateToFavourite()
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

    /**
     * Destroy
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding.listView.adapter = null
        _binding = null
    }
}