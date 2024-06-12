package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.CastItem
import com.slepetbangkit.cinematch.data.remote.response.CrewItem
import com.slepetbangkit.cinematch.data.remote.response.SimilarMoviesItem
import com.slepetbangkit.cinematch.databinding.ViewMovieDetailsBinding
import com.slepetbangkit.cinematch.view.moviedetails.adapters.CastAdapter
import com.slepetbangkit.cinematch.view.moviedetails.adapters.CrewAdapter
import com.slepetbangkit.cinematch.view.moviedetails.adapters.SimilarMoviesAdapter

class MovieDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding: ViewMovieDetailsBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewMovieDetailsBinding.inflate(inflater, this, true)
    }

    fun setMoviePoster(url: String) {
        Glide.with(binding.moviePosterIv.context)
            .load(url)
            .placeholder(R.drawable.image_broken_poster)
            .error(R.drawable.image_broken_poster)
            .into(binding.moviePosterIv)
    }

    fun setMovieTitle(title: String) {
        binding.movieTitleTv.text = title
    }

    fun setReleaseYear(releaseDate: String) {
        val year = if (releaseDate.isNotEmpty() && releaseDate.length >= 4) {
            releaseDate.substring(0, 4)
        } else {
            "Unknown"
        }

        binding.releaseYearTv.text = year
    }

    fun setDirector(director: String) {
        binding.directorTv.text = director
    }

    fun setSynopsis(description: String) {
        binding.synopsisTv.text = description
    }

    fun setVerdict(rating: Any) {
        binding.verdictTv.text = rating.toString()
    }

    fun setTrailerLink(trailerUrl: String?) {
        if (trailerUrl.isNullOrEmpty()) {
            binding.trailerFrame.visibility = View.GONE
            binding.trailerDivider.visibility = View.GONE
            binding.trailerTv.visibility = View.GONE
        } else {
            binding.trailerFrame.visibility = View.VISIBLE
            binding.trailerDivider.visibility = View.VISIBLE
            binding.trailerTv.visibility = View.VISIBLE
            binding.trailerFrame.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                context.startActivity(intent)
            }
        }
    }

    fun setCast(castList: List<CastItem?>) {
        binding.castRv.apply {
            adapter = CastAdapter().apply {
                submitList(castList)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    fun setCrew(crewList: List<CrewItem?>) {
        binding.crewRv.apply {
            adapter = CrewAdapter().apply {
                submitList(crewList)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    fun setSimilarMovies(movieList: List<SimilarMoviesItem?>, onClick: (SimilarMoviesItem) -> Unit) {
        binding.similarMoviesRv.apply {
            adapter = SimilarMoviesAdapter(onClick).apply {
                submitList(movieList)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
