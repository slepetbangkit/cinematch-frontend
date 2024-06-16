package com.slepetbangkit.cinematch.view.profile.movielist.otherprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MoviesItem
import com.slepetbangkit.cinematch.databinding.ItemMovieListOtherBinding

class OtherProfileMovieListAdapter: ListAdapter<MoviesItem, OtherProfileMovieListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMovieListOtherBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
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

    class MyViewHolder(val binding: ItemMovieListOtherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MoviesItem) {
            Glide.with(binding.moviePoster.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePoster)

            binding.tvMovieTitle.text = movie.title

            val releaseDate = movie.releaseDate
            val year = if (releaseDate.length >= 4) {
                releaseDate.substring(0, 4)
            } else {
                "Unknown"
            }

            binding.tvReleaseYear.text = year
            binding.directorTv.text = movie.director
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MoviesItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MoviesItem>() {
            override fun areItemsTheSame(oldItem: MoviesItem, newItem: MoviesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: MoviesItem, newItem: MoviesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}