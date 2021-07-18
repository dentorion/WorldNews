package com.example.worldnews.presentation.ui.favourite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.worldnews.R
import com.example.worldnews.databinding.FavouriteNewsItemBinding
import com.example.worldnews.databinding.NewsItemBinding
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.presentation.base.adapter.BaseNewsAdapter
import com.example.worldnews.presentation.extension.setOnDebouncedClickListener
import com.example.worldnews.presentation.extension.show
import com.example.worldnews.presentation.extension.up
import com.example.worldnews.presentation.extension.visible
import com.example.worldnews.presentation.util.MapperDate

class FavouriteNewsAdapter :
    BaseNewsAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavouriteNewsItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
}