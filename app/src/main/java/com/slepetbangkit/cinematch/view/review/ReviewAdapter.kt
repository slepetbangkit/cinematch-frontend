package com.slepetbangkit.cinematch.view.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.ReviewsItem
import com.slepetbangkit.cinematch.databinding.ItemReviewCardBinding
import com.slepetbangkit.cinematch.util.GlideApp

class ReviewAdapter : ListAdapter<ReviewsItem, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = getItem(position)
        holder.bind(reviewItem)
    }

    class ReviewViewHolder(
        private val binding: ItemReviewCardBinding,
        private val onItemClickCallback: OnItemClickCallback?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewItem: ReviewsItem) {
            GlideApp.with(binding.ivProfilePicture)
                .load(reviewItem.profilePicture)
                .error(R.drawable.account_circle_24)
                .circleCrop()
                .into(binding.ivProfilePicture)

            binding.tvUsername.text = reviewItem.username
            binding.tvSentiment.text = reviewItem.sentiment
            binding.tvReview.text = reviewItem.description

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(reviewItem)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ReviewsItem)
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
