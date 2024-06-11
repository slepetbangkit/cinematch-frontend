package com.slepetbangkit.cinematch.view.activity

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.ActivitiesItem
import com.slepetbangkit.cinematch.databinding.ItemActivityCardBinding

class ActivityAdapter: ListAdapter<ActivitiesItem, ActivityAdapter.ActivityViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: ActivityAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: ActivityAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(activity)
//        }
    }

    class ActivityViewHolder(val binding: ItemActivityCardBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: ActivitiesItem) {
//            if (activity.type == "FOLLOWED_USER") {
//                val username = activity.username
//                val followedUsername = activity.followedUsername
//                val fullText = "$username started following $followedUsername"
//
//                val spannableString = SpannableString(fullText)
//
//                val semiBoldTypeface = ResourcesCompat.getFont(context, R.font.plus_jakarta_sans_semi_bold)
//                if (username != null && followedUsername != null) {
//                    spannableString.setSpan(semiBoldTypeface, 0, username.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//                    spannableString.setSpan(semiBoldTypeface, username.length + 18, fullText.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//                }
//
//                binding.activityTv.text = spannableString
//            } else if (activity.type == "REVIEWED_MOVIE") {
//                binding.activityTv.text = activity.description
//            }
            binding.activityTv.text = activity.description
            binding.timeTv.text = activity.createdAt
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