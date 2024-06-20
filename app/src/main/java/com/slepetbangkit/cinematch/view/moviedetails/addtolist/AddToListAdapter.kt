package com.slepetbangkit.cinematch.view.moviedetails.addtolist

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.PlaylistItem
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.databinding.ItemAddToListMovieBinding

class AddToListAdapter(
    private val inPlaylists: List<PlaylistItem>,
    private val onItemToggleCallback: (PlaylistsItem, Boolean) -> Unit
) : ListAdapter<PlaylistsItem, AddToListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val checkedStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAddToListMovieBinding.inflate(parent.context.getSystemService(LayoutInflater::class.java), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        if (list != null) {
            holder.bind(list, inPlaylists, checkedStateArray, onItemToggleCallback)
        }
    }

    class MyViewHolder(val binding: ItemAddToListMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            list: PlaylistsItem,
            inPlaylists: List<PlaylistItem>,
            checkedStateArray: SparseBooleanArray,
            onItemToggleCallback: (PlaylistsItem, Boolean) -> Unit
        ) {
            binding.tvListTitle.text = list.title
            binding.tvMovieCount.text = "${list.movies.count()} movies"

            val isChecked = checkedStateArray.get(adapterPosition, inPlaylists.any { it.id == list.id })
            binding.cbAddToList.isChecked = isChecked

            if (list.movies.isNotEmpty()) {
                Glide.with(binding.ivListPoster.context)
                    .load(list.movies[0].posterUrl)
                    .placeholder(R.drawable.poster_empty_placeholder)
                    .error(R.drawable.image_broken_poster)
                    .into(binding.ivListPoster)
            } else {
                Glide.with(binding.ivListPoster.context)
                    .load(R.drawable.poster_list_placeholder)
                    .placeholder(R.drawable.poster_empty_placeholder)
                    .into(binding.ivListPoster)
            }

            binding.root.setOnClickListener {
                val newCheckedState = !binding.cbAddToList.isChecked
                binding.cbAddToList.isChecked = newCheckedState
                checkedStateArray.put(adapterPosition, newCheckedState)
                onItemToggleCallback(list, newCheckedState)
            }

            binding.cbAddToList.setOnClickListener {
                val newCheckedState = binding.cbAddToList.isChecked
                checkedStateArray.put(adapterPosition, newCheckedState)
                onItemToggleCallback(list, newCheckedState)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaylistsItem>() {
            override fun areItemsTheSame(oldItem: PlaylistsItem, newItem: PlaylistsItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: PlaylistsItem, newItem: PlaylistsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
