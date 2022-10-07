package com.zumer.direct4mechallenge.uiLayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zumer.direct4mechallenge.dataLayer.model.Article
import com.zumer.direct4mechallenge.databinding.RowArticlesItemBinding

class ArticlesListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Article, ArticlesListAdapter.ArticleViewHolder>(ArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(RowArticlesItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(article)
        }
        holder.bind(article)
    }

    inner class ArticleViewHolder(private var binding: RowArticlesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
//            Glide.with(binding.ivArticle)
//                .load(article.urlToImage)
//                .into(binding.ivArticle)
            binding.ivArticle.load(article.urlToImage)
            binding.tvDate.text = article.publishedAt
            binding.tvTitle.text = article.title
        }
    }

    class ArticleComparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class OnClickListener(val clickListener: (article: Article) -> Unit) {
        fun onClick(article: Article) = clickListener(article)
    }
}