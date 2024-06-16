package com.slepetbangkit.cinematch.view.activity

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.ActivitiesItem
import com.slepetbangkit.cinematch.databinding.ItemActivityCardBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.util.GlideApp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope

class ActivityAdapter: ListAdapter<ActivitiesItem, ActivityAdapter.ActivityViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(activity)
        }
    }

    class ActivityViewHolder(val binding: ItemActivityCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: ActivitiesItem) {
            GlideApp.with(binding.ivProfilePicture.context)
                .load(activity.profilePicture)
                .error(R.drawable.baseline_account_circle_24)
                .circleCrop()
                .into(binding.ivProfilePicture)
            binding.activityTv.text = activity.description
            binding.timeTv.text = activity.date
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ActivitiesItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ActivitiesItem>() {
            override fun areItemsTheSame(oldItem: ActivitiesItem, newItem: ActivitiesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ActivitiesItem, newItem: ActivitiesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}