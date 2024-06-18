package com.slepetbangkit.cinematch.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.RecommendedItem
import com.slepetbangkit.cinematch.data.remote.response.SimilarMoviesItem
import com.slepetbangkit.cinematch.databinding.ItemLargeMoviePosterBinding
import com.slepetbangkit.cinematch.databinding.ItemMoviePosterBinding
import com.slepetbangkit.cinematch.util.GlideApp
import com.slepetbangkit.cinematch.view.home.HomeViewModel

class LargeMoviePosterAdapter : ListAdapter<RecommendedItem, LargeMoviePosterAdapter.MovieCardViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        val binding = ItemLargeMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(movie)
        }
    }

    class MovieCardViewHolder(private val binding: ItemLargeMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: RecommendedItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RecommendedItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendedItem>() {
            override fun areItemsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}