package com.entin.worldnews.presentation.ui.detail

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.entin.worldnews.R
import com.entin.worldnews.databinding.DetailFragmentBinding
import com.entin.worldnews.domain.model.ViewModelResult
import com.entin.worldnews.presentation.base.fragment.BaseFragment
import com.entin.worldnews.presentation.base.fragment.extension.renderStateExtension
import com.entin.worldnews.presentation.extension.observe
import com.entin.worldnews.presentation.extension.visible
import com.entin.worldnews.presentation.util.IMAGE_CORNER_RADIUS_1
import com.entin.worldnews.presentation.util.IMAGE_CORNER_RADIUS_24
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InternalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : BaseFragment(R.layout.detail_fragment) {

    // Binding
    private val binding: DetailFragmentBinding by viewBinding()

    // ViewModel
    override val viewModel: DetailViewModel by viewModels()

    /**
     * State observer
     */
    private val stateObserver = Observer<ViewModelResult<DetailViewState>> { result ->
        setState(result)
    }

    // All functions here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setupArticleObserver()

        setupFavouriteIconObserver()

        viewModel.getArticle()
    }

    /**
     * Ui State Observer LiveResult
     */
    private fun setupArticleObserver() {
        observe(viewModel.uiStateDetailFragment, stateObserver)
    }

    /**
     * Each WorldNewsResult<DetailViewState> goes to extension function
     * and then to the BaseFragment, where it renders all views
     */
    private fun setState(uiState: ViewModelResult<DetailViewState>) {
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
    private fun onSuccess(viewState: DetailViewState) {
        initContent(viewState)
        initClickListeners(viewState)
        initImage(viewState)
    }

    /**
     * Observer for like icon
     */
    private fun setupFavouriteIconObserver() = viewModel.favouriteChannel
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach { mode ->
            setFavouriteIcon(mode)
        }
        .launchIn(lifecycleScope)

    /**
     * Set content text
     * Click listeners: favourite + open web page + share
     * Set main image
     */
    private fun initContent(article: DetailViewState) {
        with(binding) {
            detailTitle.text = article.title
            author.text = if (article.author.isEmpty()) {
                binding.authorLabelImg.visible = false
                ""
            } else {
                article.author
            }
            detailPublishedAt.text = article.publishedAt
            detailPublishTime.text = article.publishedTime
            detailContent.text = article.description
        }
    }

    /**
     * Click listeners for:
     *  - favourite button
     *  - open source button
     *  - share button
     */
    private fun initClickListeners(article: DetailViewState) {
        with(binding) {

            // Favourite
            favouriteBtn.setOnClickListener {
                viewModel.favouriteBtnClick(article.url)
            }

            // Open WebView
            openArticleBtn.setOnClickListener {
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToArticleWebView(article.url)
                )
            }

            // Share url Article
            shareBtn.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, article.url)
                intent.type = "text/plain"
                startActivity(
                    Intent.createChooser(
                        intent,
                        requireContext().getString(R.string.detail_share_to)
                    )
                )
            }
        }
    }

    /**
     * Initialize image with Glide
     */
    private fun initImage(detailViewState: DetailViewState) {
        // Image corner radius
        val cornerRadius: Int =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                IMAGE_CORNER_RADIUS_24
            } else {
                IMAGE_CORNER_RADIUS_1
            }
        // Set Image
        try {
            Glide.with(this)
                .load(detailViewState.urlToImage)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(binding.detailImg)
        } catch (e: Exception) {

        }
    }

    /**
     * Favourite icon
     */
    private fun setFavouriteIcon(mode: Boolean) {
        when (mode) {
            true -> binding.favouriteBtn.setImageResource(R.drawable.favorite_icon_light)
            false -> binding.favouriteBtn.setImageResource(R.drawable.favourite_icon_stroke_light)
        }
    }

    /**
     * Menu bar
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_detail, menu)
    }

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
            else -> super.onOptionsItemSelected(item)
        }
    }
}