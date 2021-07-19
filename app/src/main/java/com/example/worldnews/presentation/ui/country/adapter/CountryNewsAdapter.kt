package com.example.worldnews.presentation.ui.country.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.worldnews.databinding.NewsItemBinding
import com.example.worldnews.presentation.base.adapter.BaseNewsAdapter

class CountryNewsAdapter :
    BaseNewsAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsItemBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }
}