package com.slepetbangkit.cinematch.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ItemMoviePosterBinding
import com.slepetbangkit.cinematch.util.GlideApp
import com.slepetbangkit.cinematch.view.home.HomeViewModel

class TopRatedMoviePosterAdapter : ListAdapter<HomeViewModel.Companion.TopRatedItem, TopRatedMoviePosterAdapter.MovieCardViewHolder>(
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
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(movie)
//        }
    }

    class MovieCardViewHolder(private val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: HomeViewModel.Companion.TopRatedItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: HomeViewModel.Companion.TopRatedItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeViewModel.Companion.TopRatedItem>() {
            override fun areItemsTheSame(oldItem: HomeViewModel.Companion.TopRatedItem, newItem: HomeViewModel.Companion.TopRatedItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HomeViewModel.Companion.TopRatedItem, newItem: HomeViewModel.Companion.TopRatedItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

//class MoviePosterAdapter : ListAdapter<SimilarMoviesItem, MoviePosterAdapter.MovieCardViewHolder>(
//    DIFF_CALLBACK
//) {
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
//        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MovieCardViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
//        val movie = getItem(position)
//        holder.bind(movie)
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(movie)
//        }
//    }
//
//    class MovieCardViewHolder(private val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(movie: SimilarMoviesItem) {
//            GlideApp.with(binding.moviePoster.context)
//                .load(movie.posterUrl)
//                .placeholder(R.drawable.poster_empty_placeholder)
//                .error(R.drawable.image_broken_poster)
//                .into(binding.moviePoster)
//        }
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(data: SimilarMoviesItem)
//    }
//
//    companion object {
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimilarMoviesItem>() {
//            override fun areItemsTheSame(oldItem: SimilarMoviesItem, newItem: SimilarMoviesItem): Boolean {
//                return oldItem.tmdbId == newItem.tmdbId
//            }
//
//            override fun areContentsTheSame(oldItem: SimilarMoviesItem, newItem: SimilarMoviesItem): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}