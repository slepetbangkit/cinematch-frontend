package com.slepetbangkit.cinematch.view.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.databinding.ItemSearchMovieBinding
import com.slepetbangkit.cinematch.util.GlideApp

class MovieAdapter: ListAdapter<MovieSearchResponseItem, MovieAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchMovieBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(movie)
            }
        }
    }

    class MyViewHolder(val binding: ItemSearchMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieSearchResponseItem) {
            GlideApp.with(binding.moviePosterIv.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterIv)

            binding.movieTitleTv.text = movie.title

            val releaseDate = movie.releaseDate
            val year = if (releaseDate.length >= 4) {
                releaseDate.substring(0, 4)
            } else {
                "Unknown"
            }

            binding.movieYearTv.text = year
            binding.movieDirectorTv.text = movie.director
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieSearchResponseItem)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieSearchResponseItem>() {
            override fun areItemsTheSame(oldItem: MovieSearchResponseItem, newItem: MovieSearchResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: MovieSearchResponseItem, newItem: MovieSearchResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}