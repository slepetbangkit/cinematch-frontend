package com.slepetbangkit.cinematch.view.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.SimilarMoviesItem
import com.slepetbangkit.cinematch.databinding.ItemMoviePosterBinding
import com.slepetbangkit.cinematch.util.GlideApp

class SimilarMoviesAdapter(private val onClick: (SimilarMoviesItem) -> Unit) :
    ListAdapter<SimilarMoviesItem, SimilarMoviesAdapter.MoviesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.bind(movieItem)
    }

    class MoviesViewHolder(
        private val binding: ItemMoviePosterBinding,
        private val onClick: (SimilarMoviesItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: SimilarMoviesItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movieItem.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)

            binding.root.setOnClickListener {
                onClick(movieItem)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SimilarMoviesItem>() {
            override fun areItemsTheSame(oldItem: SimilarMoviesItem, newItem: SimilarMoviesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SimilarMoviesItem, newItem: SimilarMoviesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
