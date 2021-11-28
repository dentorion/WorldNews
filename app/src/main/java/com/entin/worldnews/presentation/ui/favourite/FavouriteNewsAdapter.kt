package com.entin.worldnews.presentation.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.entin.worldnews.databinding.ItemWriteNewsBinding
import com.entin.worldnews.presentation.base.adapter.BaseNewsAdapter

class FavouriteNewsAdapter : BaseNewsAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWriteNewsBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
}