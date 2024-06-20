package com.slepetbangkit.cinematch.view.profile.movielist.selfprofile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MoviesItem
import com.slepetbangkit.cinematch.databinding.ItemMovieListBinding
import com.slepetbangkit.cinematch.util.GlideApp

class SelfProfileMovieListAdapter(private val isBlend: Boolean):
    ListAdapter<MoviesItem, SelfProfileMovieListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onRemoveClickCallback: OnRemoveClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setOnRemoveClickCallback(onRemoveClickCallback: OnRemoveClickCallback) {
        this.onRemoveClickCallback = onRemoveClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMovieListBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie, isBlend)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(movie)
            }
            holder.binding.btnRemoveMovie.setOnClickListener {
                onRemoveClickCallback.onRemoveClicked(movie)
            }
        }
    }

    class MyViewHolder(val binding: ItemMovieListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MoviesItem, isBlend: Boolean) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)

            binding.tvMovieTitle.text = movie.title

            val releaseDate = movie.releaseDate
            val year = if (releaseDate.length >= 4) {
                releaseDate.substring(0, 4)
            } else {
                "Unknown"
            }

            binding.tvReleaseYear.text = year
            binding.directorTv.text = movie.director
            binding.btnRemoveMovie.visibility = if (isBlend) View.GONE else View.VISIBLE

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MoviesItem)
    }

    interface OnRemoveClickCallback {
        fun onRemoveClicked(data: MoviesItem)
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