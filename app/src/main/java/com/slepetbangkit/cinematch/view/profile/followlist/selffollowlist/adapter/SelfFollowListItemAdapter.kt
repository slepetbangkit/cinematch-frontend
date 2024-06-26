package com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.FollowListItem
import com.slepetbangkit.cinematch.databinding.ItemUserBinding
import com.slepetbangkit.cinematch.util.GlideApp

class SelfFollowListItemAdapter: ListAdapter<FollowListItem, SelfFollowListItemAdapter.FollowListHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowListHolder {
        val binding = ItemUserBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return FollowListHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowListHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }
        }
    }

    class FollowListHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FollowListItem) {
            if (user.profilePicture != null) {
                GlideApp.with(binding.ivProfilePicture.context)
                    .load(user.profilePicture)
                    .error(R.drawable.account_circle_24)
                    .circleCrop()
                    .into(binding.ivProfilePicture)
            }
            binding.tvUsername.text = user.username
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FollowListItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowListItem>() {
            override fun areItemsTheSame(oldItem: FollowListItem, newItem: FollowListItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: FollowListItem, newItem: FollowListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}