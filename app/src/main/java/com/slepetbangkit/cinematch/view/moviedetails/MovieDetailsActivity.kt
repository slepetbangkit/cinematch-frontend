package com.slepetbangkit.cinematch.view.moviedetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.slepetbangkit.cinematch.databinding.ActivityMovieDetailsBinding
import com.slepetbangkit.cinematch.databinding.ModalAddToListBinding


class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.addToListIb.setOnClickListener {
            showAddToListModal()
        }
    }

    private fun showAddToListModal() {
        val dialog = BottomSheetDialog(this)
        val modalBinding: ModalAddToListBinding = ModalAddToListBinding.inflate(layoutInflater)
        dialog.setContentView(modalBinding.root)

        // Setup RecyclerView
        modalBinding.movieListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dialog.show()
    }

    companion object {
        const val MOVIE_ID = "movie_id"
    }
}
