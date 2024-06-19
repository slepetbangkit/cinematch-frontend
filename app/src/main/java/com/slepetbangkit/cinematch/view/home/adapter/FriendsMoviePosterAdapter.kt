package com.slepetbangkit.cinematch.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.FriendsItem
import com.slepetbangkit.cinematch.databinding.ItemFriendActivityMoviePosterBinding
import com.slepetbangkit.cinematch.util.GlideApp

class FriendsMoviePosterAdapter : ListAdapter<FriendsItem, FriendsMoviePosterAdapter.MovieCardViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        val binding = ItemFriendActivityMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(movie)
        }
    }

    class MovieCardViewHolder(private val binding: ItemFriendActivityMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: FriendsItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)

//            GlideApp.with(binding.profileImg.context)
//                .load(movie.profilePicture)
//                .placeholder(R.drawable.account_circle_24)
//                .error(R.drawable.account_circle_24)
//                .circleCrop()
//                .into(binding.profileImg)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FriendsItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendsItem>() {
            override fun areItemsTheSame(oldItem: FriendsItem, newItem: FriendsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FriendsItem, newItem: FriendsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}