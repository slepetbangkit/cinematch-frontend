package com.slepetbangkit.cinematch.view.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.ActivitiesItem
import com.slepetbangkit.cinematch.databinding.ItemActivityCardBinding
import com.slepetbangkit.cinematch.util.GlideApp

class ActivityAdapter(private val sessionUsername: String): ListAdapter<ActivitiesItem, ActivityAdapter.ActivityViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(sessionUsername, binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(activity)
        }
    }

    class ActivityViewHolder(private val sessionUsername: String, val binding: ItemActivityCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: ActivitiesItem) {
            GlideApp.with(binding.ivProfilePicture.context)
                .load(activity.profilePicture)
                .error(R.drawable.account_circle_24)
                .circleCrop()
                .into(binding.ivProfilePicture)
            if (activity.followedUsername == sessionUsername) {
                val description = activity.description.substringBeforeLast(sessionUsername)
                binding.activityTv.text = buildString {
                    append(description)
                    append("you")
                }
            } else {
                binding.activityTv.text = activity.description
            }
            binding.timeTv.text = activity.date
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ActivitiesItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ActivitiesItem>() {
            override fun areItemsTheSame(oldItem: ActivitiesItem, newItem: ActivitiesItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ActivitiesItem, newItem: ActivitiesItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}