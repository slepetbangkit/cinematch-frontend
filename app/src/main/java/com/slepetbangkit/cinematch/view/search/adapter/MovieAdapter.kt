package com.slepetbangkit.cinematch.view.search.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.SearchResponseItem
import com.slepetbangkit.cinematch.databinding.SearchMovieItemBinding
import com.slepetbangkit.cinematch.view.movieDetails.MovieDetailsActivity

class MovieAdapter: ListAdapter<SearchResponseItem, MovieAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SearchMovieItemBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, MovieDetailsActivity::class.java)
                intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.tmdbId)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    class MyViewHolder(val binding: SearchMovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: SearchResponseItem) {
            Glide.with(binding.moviePosterIv.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.broken_poster)
                .error(R.drawable.broken_poster)
                .into(binding.moviePosterIv)

            binding.movieTitleTv.text = movie.title

            val releaseDate = movie.releaseDate
            val year = if (releaseDate != null && releaseDate.length >= 4) {
                releaseDate.substring(0, 4)
            } else {
                "Unknown"
            }

            binding.movieYearTv.text = year
            binding.movieDirectorTv.text = movie.director
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResponseItem>() {
            override fun areItemsTheSame(oldItem: SearchResponseItem, newItem: SearchResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: SearchResponseItem, newItem: SearchResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}