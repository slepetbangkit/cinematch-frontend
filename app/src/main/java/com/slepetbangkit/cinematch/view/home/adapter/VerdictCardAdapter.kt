package com.slepetbangkit.cinematch.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.VerdictItem
import com.slepetbangkit.cinematch.databinding.ItemVerdictCardBinding
import com.slepetbangkit.cinematch.util.GlideApp
import com.slepetbangkit.cinematch.view.home.HomeViewModel

class VerdictCardAdapter : ListAdapter<VerdictItem, VerdictCardAdapter.VerdictViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerdictViewHolder {
        val binding = ItemVerdictCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VerdictViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerdictViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(movie)
        }
    }

    class VerdictViewHolder(private val binding: ItemVerdictCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(verdict: VerdictItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(verdict.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)

            binding.movieTitleTv.text = verdict.title
//            binding.movieYearTv.text = verdict.releaseDate
//            binding.movieReviewTv.text = verdict.reviewId
//
//            GlideApp.with(binding.profileImg.context)
//                .load(movie.profilePicture)
//                .placeholder(R.drawable.account_circle_24)
//                .error(R.drawable.account_circle_24)
//                .circleCrop()
//                .into(binding.profileImg)

            binding.usernameTv.text = verdict.username
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: VerdictItem)
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<VerdictItem>() {
                override fun areItemsTheSame(
                    oldItem: VerdictItem,
                    newItem: VerdictItem
                ): Boolean {
                    return oldItem.reviewId == newItem.reviewId
                }

                override fun areContentsTheSame(
                    oldItem: VerdictItem,
                    newItem: VerdictItem
                ): Boolean {
                    return oldItem.reviewId == newItem.reviewId
                }
            }
    }
}