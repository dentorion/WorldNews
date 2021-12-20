package com.entin.worldnews.presentation.ui.country.components

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.entin.network.api.CATEGORY_GENERAL
import com.entin.network.api.CATEGORY_HEALTH
import com.entin.network.api.CATEGORY_SPORTS
import com.entin.worldnews.R
import com.entin.worldnews.databinding.FragmentCountryNewsBinding
import com.entin.worldnews.databinding.PartLoadingBinding
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.domain.model.NewsTopic
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.base.fragment.extension.renderStateExtension
import com.entin.worldnews.presentation.extension.observe
import com.entin.worldnews.presentation.ui.dialogs.delete.DeleteFinishedDialog
import com.entin.worldnews.presentation.util.alert.simpleShortSnackBar
import com.entin.worldnews.presentation.util.connection.ConnectionCheckLiveData
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

    // Binding
    private var _binding: FragmentCountryNewsBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    override val viewModel: CountryViewModel by viewModels()

    // RecycleView Adapter - List Adapter
    private val newsAdapter = CountryNewsAdapter()

    // Check connection to Internet
    @Inject
    lateinit var connectionCheckManager: ConnectionCheckLiveData


    // State observer
    private val stateObserver = Observer<ViewModelResult<CountryViewState>> { result ->
        setState(result)
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

        /**
         * Internet connection observer and show alert about Internet connection lost
         * On initial it is null if Internet connection failed
         */
        if (connectionCheckManager.value == null) {
            showInternetConnectionFail(true)
        }
        connectionCheckManager.observe(viewLifecycleOwner) { connection ->
            showInternetConnectionFail(!connection)
        }

        return binding.root
    }

    private fun showInternetConnectionFail(value: Boolean) {
        binding.noInternetPart.apply {
            isVisible = value
        }
        binding.readNewsOffline.setOnClickListener {
            viewModel.getOfflineNews(currentCountry)
        }
    }


    // All functions here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initNewsObserver()

        initRepeatButton()

        initRecyclerClickListener()

        initRecyclerLayoutManager()

        initSwipeGuest()

        loadData(isForced = false)
    }

    /**
     * Subscribe observers and react on changing
     *  - Internet connection
     *  - Ui state from viewModel
     */
    private fun initNewsObserver() {
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
     * Repeat button in merged layout triggers viewModel data loading function
     */
    private fun initRepeatButton() {
        super.initRepeatButton(partLoadingBinding = PartLoadingBinding.bind(binding.root))
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
            newsRecyclerView.apply {
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
                    viewModel.onSwipeGuest(country = currentCountry)
                    isRefreshing = false
                }
            }
        }
    }

    /**
     * News get from viewModel
     */
    private fun loadData(isForced: Boolean) {
        viewModel.loadData(currentCountry, isForced)
    }

    /**
     * Each WorldNewsResult<CountryViewState> goes to extension function
     * and then to the BaseFragment, where it is render all views
     */
    private fun setState(uiState: ViewModelResult<CountryViewState>) {
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
    private fun onSuccess(viewState: CountryViewState) {
        if (viewState.isForced) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.alert_forced_updated_news)
            )
        }
        if (viewState.isEmpty) {
            simpleShortSnackBar(
                requireView(),
                requireContext().getString(R.string.alert_empty_news)
            )
        }
        if (viewState.news.isNotEmpty()) {
            val allNews = when (viewModel.stateOfNewsTopic) {
                NewsTopic.All -> viewState.news
                NewsTopic.General -> viewState.news.filter { it.category == CATEGORY_GENERAL }
                NewsTopic.Health -> viewState.news.filter { it.category == CATEGORY_HEALTH }
                NewsTopic.Sports -> viewState.news.filter { it.category == CATEGORY_SPORTS }
            }
            newsAdapter.submitList(allNews)
        }
    }

    /**
     * Menu bar
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_country, menu)
    }

    /**
     * Menu bar menu listener
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
                loadData(isForced = true)
                true
            }
            R.id.action_delete_all_tasks -> {
                DeleteFinishedDialog(currentCountry).show(childFragmentManager, "TAG")
                true
            }
            R.id.action_all_topics -> {
                viewModel.setNewsTopic(NewsTopic.All)
                true
            }
            R.id.action_general_topics -> {
                viewModel.setNewsTopic(NewsTopic.General)
                true
            }
            R.id.action_health_topics -> {
                viewModel.setNewsTopic(NewsTopic.Health)
                true
            }
            R.id.action_sports_topics -> {
                viewModel.setNewsTopic(NewsTopic.Sports)
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
        binding.newsRecyclerView.adapter = null
        _binding = null
    }
}