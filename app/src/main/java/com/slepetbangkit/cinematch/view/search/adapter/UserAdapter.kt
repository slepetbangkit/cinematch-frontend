package com.slepetbangkit.cinematch.view.search.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.data.remote.response.UsersItem
import com.slepetbangkit.cinematch.databinding.ItemSearchMovieBinding
import com.slepetbangkit.cinematch.databinding.ItemUserBinding

class UserAdapter: ListAdapter<UsersItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }
        }
    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UsersItem) {
//            Glide.with(binding.ivProfilePicture.context)
//                .load(user.)
//                .placeholder(R.drawable.image_broken_poster)
//                .error(R.drawable.image_broken_poster)
//                .into(binding.moviePosterIv)

            binding.tvUsername.text = user.username
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UsersItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UsersItem>() {
            override fun areItemsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}