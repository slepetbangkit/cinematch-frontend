package com.slepetbangkit.cinematch.view.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.CastItem
import com.slepetbangkit.cinematch.databinding.ItemCastCrewBinding
import com.slepetbangkit.cinematch.util.GlideApp

class CastAdapter: ListAdapter<CastItem, CastAdapter.CastViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castItem = getItem(position)
        holder.bind(castItem)
    }

    class CastViewHolder(private val binding: ItemCastCrewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(castItem: CastItem) {
            binding.nameCastCrew.text = castItem.name
            binding.detailCastCrew.text = castItem.character
            GlideApp.with(binding.photoCastCrew.context)
                .load(castItem.profileUrl)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(binding.photoCastCrew)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastItem>() {
            override fun areItemsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: CastItem, newItem: CastItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}