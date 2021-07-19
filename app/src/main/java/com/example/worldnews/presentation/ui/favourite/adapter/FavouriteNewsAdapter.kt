package com.example.worldnews.presentation.ui.favourite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.worldnews.databinding.FavouriteNewsItemBinding
import com.example.worldnews.presentation.base.adapter.BaseNewsAdapter

class FavouriteNewsAdapter :
    BaseNewsAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavouriteNewsItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
}