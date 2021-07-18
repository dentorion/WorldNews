package com.example.worldnews.presentation.ui.detail

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.worldnews.R
import com.example.worldnews.databinding.DetailFragmentBinding
import com.example.worldnews.presentation.extension.visible
import com.example.worldnews.presentation.ui.detail.viewmodel.DetailViewModel
import com.example.worldnews.presentation.ui.detail.viewmodel.ViewState
import com.example.worldnews.presentation.util.IMAGE_CORNER_RADIUS_1
import com.example.worldnews.presentation.util.IMAGE_CORNER_RADIUS_24
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InternalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val binding: DetailFragmentBinding by viewBinding()
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        /**
         * Observers of news data & like icon
         */
        setupArticleObserver()

        setupFavouriteIconObserver()

        detailViewModel.getArticle()
    }

    private fun setupArticleObserver() = detailViewModel.articleChannel
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach { checkedDetailArticle ->

            initContent(checkedDetailArticle)

            initClickListeners(checkedDetailArticle)

            initImage(checkedDetailArticle)

        }.launchIn(lifecycleScope)

    private fun setupFavouriteIconObserver() = detailViewModel.favouriteChannel
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach { mode ->
            setFavouriteIcon(mode)
        }.launchIn(lifecycleScope)

    /**
     * Set content text
     * Click listeners: favourite + open web page + share
     * Set main image
     */

    private fun initContent(article: ViewState) {
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

    private fun initClickListeners(article: ViewState) {
        with(binding) {

            // Favourite
            favouriteBtn.setOnClickListener {
                detailViewModel.favouriteBtnClick(article.url)
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
                startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }
    }

    private fun initImage(viewState: ViewState) {
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
                .load(viewState.urlToImage)
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
     * Favourite icon interface
     */

    private fun setFavouriteIcon(mode: Boolean) {
        when (mode) {
            true -> binding.favouriteBtn.setImageResource(R.drawable.favorite_icon_light)
            false -> binding.favouriteBtn.setImageResource(R.drawable.favourite_icon_stroke_light)
        }
    }

    // Menu bar

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bar_menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                detailViewModel.navigateToSearch()
                true
            }
            R.id.action_favourite -> {
                detailViewModel.navigateToFavourite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}