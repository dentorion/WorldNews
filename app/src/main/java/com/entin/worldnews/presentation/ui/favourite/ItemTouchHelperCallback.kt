package com.entin.worldnews.presentation.ui.favourite

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(
    private val favouriteNewsAdapter: FavouriteNewsAdapter,
    private val viewModel: FavouriteViewModel
) : ItemTouchHelper.SimpleCallback(
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
}