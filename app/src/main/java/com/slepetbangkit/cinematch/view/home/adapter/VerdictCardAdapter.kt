package com.slepetbangkit.cinematch.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ItemVerdictCardBinding
import com.slepetbangkit.cinematch.util.GlideApp
import com.slepetbangkit.cinematch.view.home.HomeViewModel

class VerdictCardAdapter : ListAdapter<HomeViewModel.Companion.VerdictItem, VerdictCardAdapter.VerdictViewHolder>(
    VerdictCardAdapter.DIFF_CALLBACK
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
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(movie)
//        }
    }

    class VerdictViewHolder(private val binding: ItemVerdictCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: HomeViewModel.Companion.VerdictItem) {
            GlideApp.with(binding.moviePosterImg.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_empty_placeholder)
                .error(R.drawable.image_broken_poster)
                .into(binding.moviePosterImg)

            binding.movieTitleTv.text = movie.title
            binding.movieYearTv.text = movie.releaseDate
            binding.movieReviewTv.text = movie.verdict

            GlideApp.with(binding.profileImg.context)
                .load(movie.profilePicture)
                .placeholder(R.drawable.account_circle_24)
                .error(R.drawable.account_circle_24)
                .circleCrop()
                .into(binding.profileImg)

            binding.usernameTv.text = movie.username
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: HomeViewModel.Companion.VerdictItem)
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<HomeViewModel.Companion.VerdictItem>() {
                override fun areItemsTheSame(
                    oldItem: HomeViewModel.Companion.VerdictItem,
                    newItem: HomeViewModel.Companion.VerdictItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: HomeViewModel.Companion.VerdictItem,
                    newItem: HomeViewModel.Companion.VerdictItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}