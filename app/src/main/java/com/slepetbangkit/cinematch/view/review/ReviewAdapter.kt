package com.slepetbangkit.cinematch.view.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.data.remote.response.ReviewsItem
import com.slepetbangkit.cinematch.databinding.ItemReviewCardBinding

class ReviewAdapter: ListAdapter<ReviewsItem, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = getItem(position)
        holder.bind(reviewItem)
    }

    class ReviewViewHolder(private val binding: ItemReviewCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reviewItem: ReviewsItem) {
            binding.tvUsername.text = reviewItem.username
            binding.tvRating.text = reviewItem.rating.toString()
            binding.tvReview.text = reviewItem.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewsItem>() {
            override fun areItemsTheSame(oldItem: ReviewsItem, newItem: ReviewsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ReviewsItem, newItem: ReviewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}