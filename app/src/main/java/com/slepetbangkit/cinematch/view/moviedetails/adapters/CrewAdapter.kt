package com.slepetbangkit.cinematch.view.moviedetails.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.CrewItem
import com.slepetbangkit.cinematch.databinding.ItemCastCrewBinding

class CrewAdapter: ListAdapter<CrewItem, CrewAdapter.CrewViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val binding = ItemCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        val crewItem = getItem(position)
        holder.bind(crewItem)
    }

    class CrewViewHolder(private val binding: ItemCastCrewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crewItem: CrewItem) {
            binding.nameCastCrew.text = crewItem.name
            binding.detailCastCrew.text = crewItem.job
            Glide.with(binding.photoCastCrew.context)
                .load(crewItem.profileUrl)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(binding.photoCastCrew)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CrewItem>() {
            override fun areItemsTheSame(oldItem: CrewItem, newItem: CrewItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: CrewItem, newItem: CrewItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}