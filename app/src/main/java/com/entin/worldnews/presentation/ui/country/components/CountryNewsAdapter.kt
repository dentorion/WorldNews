package com.entin.worldnews.presentation.ui.country.components

import android.view.LayoutInflater
import android.view.ViewGroup
import com.entin.worldnews.databinding.NewsItemBinding
import com.entin.worldnews.presentation.base.adapter.BaseNewsAdapter

class CountryNewsAdapter : BaseNewsAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
}