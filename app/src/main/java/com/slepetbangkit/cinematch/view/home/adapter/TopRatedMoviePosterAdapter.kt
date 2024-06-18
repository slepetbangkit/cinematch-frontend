package com.slepetbangkit.cinematch.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.TopRatedItem
import com.slepetbangkit.cinematch.databinding.ItemMoviePosterBinding
import com.slepetbangkit.cinematch.util.GlideApp
import com.slepetbangkit.cinematch.view.home.HomeViewModel

class TopRatedMoviePosterAdapter : ListAdapter<TopRatedItem, TopRatedMoviePosterAdapter.MovieCardViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(movie)
        }
    }

    class MovieCardViewHolder(private val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: TopRatedItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TopRatedItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TopRatedItem>() {
            override fun areItemsTheSame(oldItem: TopRatedItem, newItem: TopRatedItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TopRatedItem, newItem: TopRatedItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}