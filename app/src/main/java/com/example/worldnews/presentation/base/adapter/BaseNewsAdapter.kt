package com.example.worldnews.presentation.base.adapter

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.worldnews.R
import com.example.worldnews.domain.entity.Article
import com.example.worldnews.presentation.extension.setOnDebouncedClickListener
import com.example.worldnews.presentation.extension.show
import com.example.worldnews.presentation.extension.visible
import com.example.worldnews.presentation.util.MapperDate

abstract class BaseNewsAdapter :
    ListAdapter<Article, BaseNewsAdapter.TaskViewHolder>(DiffCallback()) {

    private var onDebouncedClickListener: ((article: Article) -> Unit)? = null

    fun setOnDebouncedClickListener(listener: (article: Article) -> Unit) {
        this.onDebouncedClickListener = listener
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder

    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
        viewHolder.itemView.animation =
            AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.splash_up)
        viewHolder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val view: ViewBinding) : RecyclerView.ViewHolder(view.root) {

        private val author: TextView = view.root.findViewById(R.id.author)
        private val title: TextView = view.root.findViewById(R.id.title)
        private val publishedAt: TextView = view.root.findViewById(R.id.publishedAt)
        private val eye: ImageView = view.root.findViewById(R.id.eye)
        private val imageView: ImageView = view.root.findViewById(R.id.imageView)

        fun bind(article: Article) {

            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION)  {
                itemView.setOnDebouncedClickListener {
                    onDebouncedClickListener?.invoke(article)
                }
            }

            view.apply {
                if (!article.author.isNullOrEmpty()) {
                    author.show()
                    author.text = article.author
                }

                publishedAt.text = MapperDate.cropPublishedAtToDate(article.publishedAt)
                title.text = article.title
                eye.visible = article.shown

                try {
                    Glide.with(imageView)
                        .load(article.urlToImage)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(CenterCrop(), RoundedCorners(24))
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .fallback(R.drawable.no_img)
                        .into(imageView)
                } catch (e: Exception) {
                    imageView.setImageResource(R.drawable.no_img)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }
}