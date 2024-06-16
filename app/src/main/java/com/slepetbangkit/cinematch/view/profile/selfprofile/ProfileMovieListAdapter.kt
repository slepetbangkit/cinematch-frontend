package com.slepetbangkit.cinematch.view.profile.selfprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.databinding.ItemListBinding

class ProfileMovieListAdapter : ListAdapter<PlaylistsItem, ProfileMovieListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        if (list != null) {
            holder.bind(list)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(list)
            }
        }
    }

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: PlaylistsItem) {
            binding.titleTv.text = list.title
            binding.countTv.text = "${list.movies.count()} movies"
            binding.descriptionTv.text = list.description

            if (list.movies.isNotEmpty()) {
                Glide.with(binding.moviePosterIv.context)
                    .load(list.movies[0].posterUrl)
                    .placeholder(R.drawable.poster_empty_placeholder)
                    .error(R.drawable.image_broken_poster)
                    .into(binding.moviePosterIv)
            } else {
                Glide.with(binding.moviePosterIv.context)
                    .load(R.drawable.poster_list_placeholder)
                    .placeholder(R.drawable.poster_empty_placeholder)
                    .into(binding.moviePosterIv)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PlaylistsItem)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaylistsItem>() {
            override fun areItemsTheSame(oldItem: PlaylistsItem, newItem: PlaylistsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: PlaylistsItem, newItem: PlaylistsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
