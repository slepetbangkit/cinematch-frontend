package com.slepetbangkit.cinematch.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.navigation.findNavController
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
import java.text.SimpleDateFormat
import java.util.Locale

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
            .placeholder(R.drawable.poster_empty_placeholder)
            .error(R.drawable.image_broken_poster)
            .into(binding.moviePosterIv)
    }

    fun setMovieTitle(title: String) {
        binding.movieTitleTv.text = title
    }

    fun setReleaseYear(releaseDate: String): String {
        return if (releaseDate.isEmpty()) {
            "Unknown"
        } else {
            binding.releaseYearTv.text = releaseDate.substring(0, 4)
            releaseDate.substring(0, 4)
        }
    }

    fun setDirector(director: String) {
        if (director.isEmpty()) {
            binding.directorTv.text = context.getString(R.string.unknown)
        }
        else {
            binding.directorTv.text = director
        }
    }

    fun setSynopsis(description: String) {
        if (description.isEmpty()) {
            binding.synopsisTv.text = context.getString(R.string.plot_unknown)
        }
        else {
            binding.synopsisTv.text = description
        }
    }

    fun setVerdict(sentiment: String) {
        binding.verdictTv.text = sentiment
    }

    fun setTrailerLink(trailerUrl: String?) {
        if (trailerUrl.isNullOrBlank()) {
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

    fun setTrailerBackdrop(backdropUrl: String) {
        Glide.with(binding.trailerIb.context)
            .load(backdropUrl)
            .placeholder(R.drawable.trailer_placeholder)
            .error(R.drawable.trailer_placeholder)
            .into(binding.trailerIb)
    }

    fun setCast(castList: List<CastItem?>) {
        if (castList.isEmpty()) {
            binding.castRv.visibility = View.GONE
            binding.castDivider.visibility = View.GONE
            binding.castTv.visibility = View.GONE
        } else {
            binding.castRv.visibility = View.VISIBLE
            binding.castDivider.visibility = View.VISIBLE
            binding.castTv.visibility = View.VISIBLE

            binding.castRv.apply {
                adapter = CastAdapter().apply {
                    submitList(castList)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    fun setCrew(crewList: List<CrewItem?>) {
        if (crewList.isEmpty()) {
            binding.crewRv.visibility = View.GONE
            binding.crewDivider.visibility = View.GONE
            binding.crewTv.visibility = View.GONE
        } else {
            binding.crewRv.visibility = View.VISIBLE
            binding.crewDivider.visibility = View.VISIBLE
            binding.crewTv.visibility = View.VISIBLE

            binding.crewRv.apply {
                adapter = CrewAdapter().apply {
                    submitList(crewList)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    fun setSimilarMovies(movieList: List<SimilarMoviesItem?>, onClick: (SimilarMoviesItem) -> Unit) {
        if (movieList.isEmpty()) {
            binding.similarMoviesRv.visibility = View.GONE
            binding.similarMoviesDivider.visibility = View.GONE
            binding.similarMoviesTv.visibility = View.GONE
        } else {
            binding.similarMoviesRv.visibility = View.VISIBLE
            binding.similarMoviesDivider.visibility = View.VISIBLE
            binding.similarMoviesTv.visibility = View.VISIBLE

            binding.similarMoviesRv.apply {
                adapter = SimilarMoviesAdapter(onClick).apply {
                    submitList(movieList)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    fun setReviewButtonClickListener(tmdbId: Int, title: String, releaseDate: String) {
        binding.reviewsBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("tmdbId", tmdbId)
                putString("movieTitle", title)
                putString("releaseDate", setReleaseYear(releaseDate))
            }
            val navController = findNavController()
            navController.navigate(R.id.action_movieDetailsFragment_to_reviewFragment, bundle)
        }
    }

    fun setOriginCountries(countries: List<String?>) {
        if (countries.isEmpty()) {
            binding.countriesOfOriginTv.text = context.getString(R.string.unknown)
        } else {
            binding.countriesOfOriginTv.text = countries.joinToString(", ")
        }
    }

    fun setGenres(genres: List<String?>) {
        if (genres.isEmpty()) {
            binding.genreTv.text = context.getString(R.string.unknown)
        } else {
            binding.genreTv.text = genres.joinToString(", ")
        }
    }

    fun setLanguage(language: String) {
        if (language.isEmpty()) {
            binding.languagesTv.text = context.getString(R.string.unknown)
        } else {
            binding.languagesTv.text = language
        }
    }

    private fun formatReleaseDate(releaseDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(releaseDate)
            date?.let { outputFormat.format(it) } ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

     fun setReleaseDate(releaseDate: String) {
         if (releaseDate.isEmpty()) {
             binding.releaseDateTv.text = context.getString(R.string.unknown)
         } else {
             binding.releaseDateTv.text = formatReleaseDate(releaseDate)
         }
    }

    @SuppressLint("StringFormatMatches")
    fun setRuntime(runtime: Int) {
        binding.runtimeTv.text = context.getString(R.string.minutes, runtime)
    }

}
