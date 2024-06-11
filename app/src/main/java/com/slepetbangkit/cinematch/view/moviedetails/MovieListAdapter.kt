package com.slepetbangkit.cinematch.view.moviedetails

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R

//class MovieListAdapter: ListAdapter<Movie, MovieListAdapter.MyViewHolder>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val movie = getItem(position)
//        holder.bind(movie)
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, MovieDetailsActivity::class.java)
//            intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
//            holder.itemView.context.startActivity(intent)
//        }
//    }
//
//    class MyViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(movie: Movie) {
//            Glide.with(binding.moviePosterIv.context)
//                .load(movie.posterPath)
//                .placeholder(R.drawable.image_broken_poster)
//                .error(R.drawable.image_broken_poster)
//                .into(binding.moviePosterIv)
//
//            binding.movieTitleTv.text = movie.title
//            binding.movieYearTv.text = movie.releaseDate.substring(0, 4)
//        }
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
//            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
//                return oldItem.id == newItem.id
//            }
//            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}